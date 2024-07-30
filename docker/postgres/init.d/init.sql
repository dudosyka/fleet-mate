create database fleetmate;

\c fleetmate

create extension plpython3u;

create table ecdh_keys (
   gentime timestamp,
   public_x text,
   public_y text,
   private_key text
);

create type custom_point as (
    x text,
    y text
);

create or replace procedure ecdh_regen_public() as $$
    import collections
    import random

    EllipticCurve = collections.namedtuple('EllipticCurve', 'name p a b g n h')

    curve = EllipticCurve(
        'secp256k1',
        # Field characteristic.
        p=0xfffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f,
        # Curve coefficients.
        a=0,
        b=7,
        # Base point.
        g=(0x79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798,
           0x483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8),
        # Subgroup order.
        n=0xfffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141,
        # Subgroup cofactor.
        h=1,
    )


    # Modular arithmetic ##########################################################

    def inverse_mod(k, p):
        """Returns the inverse of k modulo p.

        This function returns the only integer x such that (x * k) % p == 1.

        k must be non-zero and p must be a prime.
        """
        if k == 0:
            raise ZeroDivisionError('division by zero')

        if k < 0:
            # k ** -1 = p - (-k) ** -1  (mod p)
            return p - inverse_mod(-k, p)

        # Extended Euclidean algorithm.
        s, old_s = 0, 1
        t, old_t = 1, 0
        r, old_r = p, k

        while r != 0:
            quotient = old_r // r
            old_r, r = r, old_r - quotient * r
            old_s, s = s, old_s - quotient * s
            old_t, t = t, old_t - quotient * t

        gcd, x, y = old_r, old_s, old_t

        assert gcd == 1
        assert (k * x) % p == 1

        return x % p


    # Functions that work on curve points #########################################

    def is_on_curve(point):
        """Returns True if the given point lies on the elliptic curve."""
        if point is None:
            # None represents the point at infinity.
            return True

        x, y = point

        return (y * y - x * x * x - curve.a * x - curve.b) % curve.p == 0


    def point_neg(point):
        """Returns -point."""
        assert is_on_curve(point)

        if point is None:
            # -0 = 0
            return None

        x, y = point
        result = (x, -y % curve.p)

        assert is_on_curve(result)

        return result


    def point_add(point1, point2):
        """Returns the result of point1 + point2 according to the group law."""
        assert is_on_curve(point1)
        assert is_on_curve(point2)

        if point1 is None:
            # 0 + point2 = point2
            return point2
        if point2 is None:
            # point1 + 0 = point1
            return point1

        x1, y1 = point1
        x2, y2 = point2

        if x1 == x2 and y1 != y2:
            # point1 + (-point1) = 0
            return None

        if x1 == x2:
            # This is the case point1 == point2.
            m = (3 * x1 * x1 + curve.a) * inverse_mod(2 * y1, curve.p)
        else:
            # This is the case point1 != point2.
            m = (y1 - y2) * inverse_mod(x1 - x2, curve.p)

        x3 = m * m - x1 - x2
        y3 = y1 + m * (x3 - x1)
        result = (x3 % curve.p,
                  -y3 % curve.p)

        assert is_on_curve(result)

        return result


    def scalar_mult(k, point):
        """Returns k * point computed using the double and point_add algorithm."""
        assert is_on_curve(point)

        if k % curve.n == 0 or point is None:
            return None

        if k < 0:
            # k * point = -k * (-point)
            return scalar_mult(-k, point_neg(point))

        result = None
        addend = point

        while k:
            if k & 1:
                # Add.
                result = point_add(result, addend)

            # Double.
            addend = point_add(addend, addend)

            k >>= 1

        assert is_on_curve(result)

        return result


    # Keypair generation and ECDHE ################################################

    def make_keypair():
        """Generates a random private-public key pair."""
        private_key = random.randrange(1, curve.n)
        public_key = scalar_mult(private_key, curve.g)

        return private_key, public_key

    private, public = make_keypair()
    public_x, public_y = public
    plpy.execute(plpy.prepare("delete from ecdh_keys where true"))
    regen = plpy.prepare("insert into ecdh_keys (gentime, public_x, public_y, private_key) values ((SELECT CURRENT_TIMESTAMP), $1, $2, $3)", ["text", "text", "text"])
    plpy.execute(regen, [str(public_x), str(public_y), str(private)])

$$ language plpython3u;

create or replace function get_symmetrical_key(client_public_x text, client_public_y text) returns custom_point as $$
    import collections
    import random

    EllipticCurve = collections.namedtuple('EllipticCurve', 'name p a b g n h')

    curve = EllipticCurve(
        'secp256k1',
        # Field characteristic.
        p=0xfffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f,
        # Curve coefficients.
        a=0,
        b=7,
        # Base point.
        g=(0x79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798,
           0x483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8),
        # Subgroup order.
        n=0xfffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141,
        # Subgroup cofactor.
        h=1,
    )


    # Modular arithmetic ##########################################################

    def inverse_mod(k, p):
        """Returns the inverse of k modulo p.

        This function returns the only integer x such that (x * k) % p == 1.

        k must be non-zero and p must be a prime.
        """
        if k == 0:
            raise ZeroDivisionError('division by zero')

        if k < 0:
            # k ** -1 = p - (-k) ** -1  (mod p)
            return p - inverse_mod(-k, p)

        # Extended Euclidean algorithm.
        s, old_s = 0, 1
        t, old_t = 1, 0
        r, old_r = p, k

        while r != 0:
            quotient = old_r // r
            old_r, r = r, old_r - quotient * r
            old_s, s = s, old_s - quotient * s
            old_t, t = t, old_t - quotient * t

        gcd, x, y = old_r, old_s, old_t

        assert gcd == 1, "gcd failed"
        assert (k * x) % p == 1, "failed"

        return x % p


    # Functions that work on curve points #########################################

    def is_on_curve(point):
        """Returns True if the given point lies on the elliptic curve."""
        if point is None:
            # None represents the point at infinity.
            return True

        x, y = point

        return (y * y - x * x * x - curve.a * x - curve.b) % curve.p == 0


    def point_neg(point):
        """Returns -point."""
        assert is_on_curve(point), "not on curve"

        if point is None:
            # -0 = 0
            return None

        x, y = point
        result = (x, -y % curve.p)

        assert is_on_curve(result), "not on curve"

        return result


    def point_add(point1, point2):
        """Returns the result of point1 + point2 according to the group law."""
        assert is_on_curve(point1), "not on curve"
        assert is_on_curve(point2), "not on curve"

        if point1 is None:
            # 0 + point2 = point2
            return point2
        if point2 is None:
            # point1 + 0 = point1
            return point1

        x1, y1 = point1
        x2, y2 = point2

        if x1 == x2 and y1 != y2:
            # point1 + (-point1) = 0
            return None

        if x1 == x2:
            # This is the case point1 == point2.
            m = (3 * x1 * x1 + curve.a) * inverse_mod(2 * y1, curve.p)
        else:
            # This is the case point1 != point2.
            m = (y1 - y2) * inverse_mod(x1 - x2, curve.p)

        x3 = m * m - x1 - x2
        y3 = y1 + m * (x3 - x1)
        result = (x3 % curve.p,
                  -y3 % curve.p)

        assert is_on_curve(result), "not on curve"

        return result


    def scalar_mult(k, point):
        """Returns k * point computed using the double and point_add algorithm."""
        assert is_on_curve(point), "not on curve"

        if k % curve.n == 0 or point is None:
            return None

        if k < 0:
            # k * point = -k * (-point)
            return scalar_mult(-k, point_neg(point))

        result = None
        addend = point

        while k:
            if k & 1:
                # Add.
                result = point_add(result, addend)

            # Double.
            addend = point_add(addend, addend)

            k >>= 1

        assert is_on_curve(result), "not on curve"

        return result

    private_query = plpy.prepare("select private_key from ecdh_keys")
    private_key = plpy.execute(private_query)[0]['private_key']
    key = scalar_mult(int(private_key), (int(client_public_x), int(client_public_y)))
    return {
        'x': key[0],
        'y': key[1]
    }

$$ language plpython3u;

create or replace function get_ecdh_public() returns custom_point as $$
    return plpy.execute(plpy.prepare("select public_x as x, public_y as y from ecdh_keys"))[0]
$$ language plpython3u;


call ecdh_regen_public();

select * from get_symmetrical_key('84243547705567136496899646259328400900466014691575041561563152217285749808413', '92407250553501816235505989544905088263713638603793381516794760735349868560019');

select * from get_ecdh_public();
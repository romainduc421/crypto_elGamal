package elgamal.tests;

import elgamal.Euclide;
import elgamal.exceptions.EuclideException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class EuclideTest {
    Euclide e;
    @BeforeEach
    void setUp() {
        e = new Euclide(null);
    }
    @Test
    void euclideEtenduTestRight() {
        BigInteger[] results = e.euclideEtendu(BigInteger.valueOf(325), BigInteger.valueOf(145));
        assertEquals(BigInteger.valueOf(5), results[0]);
    }

    @Test
    void euclideEtenduTestCrosscheck(){
        BigInteger a = BigInteger.valueOf(1234);
        BigInteger b = BigInteger.valueOf(5678);
        BigInteger[] resultats = e.euclideEtendu(a,b);
        BigInteger gcd = resultats[0];
        BigInteger u = resultats[1];
        BigInteger v = resultats[2];

        // Vérifie que le GCD de a et b est correct avec la methode BigInteger.gcd()
        assertEquals(gcd, (a.gcd(b)).abs());    //2 et 2

        // Vérifie que a * u + b * v = GCD(a, b)
        assertEquals(gcd, (a.multiply(u)).add(b.multiply(v)));
    }

    @Test
    void euclidePrime(){
        assertDoesNotThrow(() -> {
            BigInteger b = new BigInteger("179769313486231590770839156793787453197860296048756011706444423684197180216158519368947833795864925541502180565485980503646440548199239100050792877003355816639229553136239076508735759914822574862575007425302077447712589550957937778424442426617334727629299387668709205606050270810842907692932019128194467627007");
            e.euclideCompute(BigInteger.valueOf(21523894), b);
        });
    }

    @Test
    void euclideNotPrime(){
        assertThrows(EuclideException.class, () -> e.euclideCompute(BigInteger.valueOf(81), BigInteger.valueOf(9)));
    }

}
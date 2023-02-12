package elgamal;

import elgamal.exceptions.EuclideException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;


public class Euclide {

    private final BufferedWriter bufferedWriter;
    public Euclide(BufferedWriter b){
        this.bufferedWriter = b;
    }

    public static BigInteger[] euclideCompute(BigInteger a, BigInteger b) throws EuclideException{
        BigInteger[] suiteResultats = euclideEtendu(a,b);
        if(suiteResultats[0].equals(BigInteger.ONE))
        {
            return suiteResultats;
        }
        else{
            throw new EuclideException("pgcd( "+a+", "+b+" ) != 1");
        }

    }
    /**
     * algorithme d'Euclide etendu
     * @param a premier entier
     * @param b deuxieme entier
     * @return tableau de BigInteger contenant pgcd(a,b) et u,v aux indices respectifs 0, 1 et 2.
     */
    public static BigInteger[] euclideEtendu(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {    //b, le reste, est nul
            //gcd vauta, u =1 et v = 0
            return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
        }
        BigInteger[] suiteResultats = euclideEtendu(b, a.remainder(b));

        BigInteger q = a.divide(b);         // a / b
        BigInteger gcd = suiteResultats[0]; // gcd = gcd(b, a%b);
        BigInteger u = suiteResultats[2];   // u = v1

        BigInteger v = suiteResultats[1].subtract( q.multiply( suiteResultats[2]) );
        // v = u1 - v1 * (a / b)

        return new BigInteger[]{gcd, u,v};
    }



    /**
     * Version iterative de l'algorithme étendu d'Euclide (eviter recursion, run faster)
     * @param a
     * @param b
     * @return {a,u,v} avec a = gcd(a,b) et coefficients u et v tq au + bv = pgcd(a,b)
     */
    public static BigInteger[] euclideEtendu2(BigInteger a, BigInteger b)
    {
        final BigInteger UN = BigInteger.ONE, ZERO = BigInteger.ZERO;
        BigInteger u = UN,
                v = ZERO,
                u1 = ZERO,
                v1 = UN,
                t;

        // tant que BigInteger.valueOf(b) > 0
        while(b.compareTo(ZERO) > 0){
            //q = a / b;
            BigInteger q = a.divide(b);
            t = u;
            u = u1;
            //u = u0 - q * u1
            u1 = t.subtract(q.multiply(u1));
            t = v;
            v = v1;
            //v = v0 - q * v1
            v1 = t.subtract(q.multiply(v1));
            t = b;
            b = a.subtract(q.multiply(b));
            a = t;
        }
        return (a.intValue()>0)? new BigInteger[]{a,u,v} : new BigInteger[]{a.negate(),u.negate(),v.negate()};
    }

    /**
     * Réalise les 10 000 tests de la fonction euclide() avec le nombre p
     * et 10 000 valeurs différentes de a (générées aléatoirement avec SecureRandom).
     * @param p value of prime BigInteger p
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public void test10000Times(BigInteger p) throws NoSuchAlgorithmException, NoSuchProviderException, EuclideException {
        BigInteger a;

        //https://stackoverflow.com/questions/12726434/use-of-sha1prng-in-securerandom-class
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");

        System.out.println("Test de la fonction Euclide() : ");
        try {
            bufferedWriter.write("Test de la fonction Euclide()  : \n");
            int k=0;
            while(k < 10000) {
                a = new BigInteger(1024, random);
                BigInteger[] results = euclideEtendu2(a, p);

                //sortie que pour les 5 dernieres occurrences
                if(k > 9994){
                    bufferedWriter.write("a = "+ a + "\t et \n");
                    bufferedWriter.write("a.u + p.v = " + (a.multiply(results[1])).add(p.multiply(results[2])) + "\n");
                    //verifie que pgcd(a,p) == results[0]
                    bufferedWriter.write("results[0] == pgcd(a,p) = "+results[0].equals(a.gcd(p).abs())+"\n");
                    // Vérifie que a * u + b * v = GCD(a, p)
                    bufferedWriter.write("au + pv == pgcd(a,p) = "+(a.gcd(p).abs()).equals((a.multiply(results[1])).add(p.multiply(results[2])))+"\n\n");
                    System.out.println("a = "+ a);
                    System.out.println("a.u + p.v = " + (a.multiply(results[1])).add(p.multiply(results[2])) );
                    //verifie que pgcd(a,p) == results[0]
                    System.out.println("results[0] == pgcd(a,p) = "+results[0].equals(a.gcd(p).abs()));
                    // Vérifie que a * u + b * v = GCD(a, p)
                    System.out.println("au + pv == pgcd(a,p) = "+(a.gcd(p).abs()).equals((a.multiply(results[1])).add(p.multiply(results[2])))+"\n");
                }
                k++;
            }
            System.out.println("Les tests se trouvent dans le fichier test.txt \n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

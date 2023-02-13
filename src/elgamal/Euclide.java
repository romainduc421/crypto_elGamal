package elgamal;

import elgamal.exceptions.EuclideException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class Euclide {

    private final BufferedWriter bufferedWriter;
    public Euclide(BufferedWriter b){
        this.bufferedWriter = b;
    }

    public static BigInteger[] euclideCompute(BigInteger a, BigInteger b) throws EuclideException{
        BigInteger[] suiteResultats = euclideEtendu2(a,b);
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
        BigInteger[] suiteResultats = euclideEtendu(b, a.remainder(b)); // gcd(b, a%b)
        BigInteger v = suiteResultats[1].subtract( (a.divide(b)).multiply( suiteResultats[2]) );
        // v = u1 - v1 * (a / b)

        return new BigInteger[]{suiteResultats[0],suiteResultats[2],v};
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


    public static BigInteger modInv(BigInteger a, BigInteger b) throws EuclideException {
        BigInteger[] resEuclide = euclideEtendu2(a,b);
        if(!resEuclide[0].equals(BigInteger.ONE)){
            throw new EuclideException("No modular inverse possible");
        }else{
            return resEuclide[1].mod(b);
        }
    }

    /**
     * Réalise les 10 000 tests de la fonction euclide() avec le nombre p
     * et 10 000 valeurs différentes de a (générées aléatoirement avec SecureRandom).
     * @param p value of prime BigInteger p
     * @throws NoSuchAlgorithmException
     */
    public void test10000Times(BigInteger p) throws NoSuchAlgorithmException, EuclideException {
        BigInteger a;

        SecureRandom random = SecureRandom.getInstanceStrong();

        System.out.println("Test de la fonction Euclide() : ");
        try {
            bufferedWriter.write("Test de la fonction Euclide()  : \n");
            int k=0;
            while(k < 10000) {
                a = new BigInteger(1024, random);
                BigInteger[] results = euclideCompute(a, p);
                BigInteger gcd_ap = a.gcd(p).abs();
                BigInteger bezout = a.multiply(results[1]).add(p.multiply(results[2]));
                assert(results[0].equals(gcd_ap)):"le premier element du tableau ne contient pas le gcd(a,p)";
                assert(gcd_ap.equals(bezout)):"gcd(a,p) != au + pv";
                assert(BigInteger.valueOf(1).equals(bezout)):"equation de bezout ne donne pas 1";

                //sortie que pour les 5 premieres occurrences
                if(k < 5){

                    bufferedWriter.write("a = "+ a + "\t et \n");
                    bufferedWriter.write("a.u + p.v = " + bezout + "\n");
                    //verifie que pgcd(a,p) == results[0]
                    bufferedWriter.write("results[0] == pgcd(a,p) = "+results[0].equals(gcd_ap)+"\n");
                    // Vérifie que a * u + b * v = GCD(a, p)
                    bufferedWriter.write("au + pv == pgcd(a,p) = "+gcd_ap.equals(bezout)+"\n");
                    bufferedWriter.write("gcd == 1 = "+BigInteger.valueOf(1).equals(bezout)+"\n\n");
                    System.out.println("a = "+ a);
                    System.out.println("a.u + p.v = " + bezout );
                    //verifie que pgcd(a,p) == results[0]
                    System.out.println("results[0] == pgcd(a,p) = "+results[0].equals(gcd_ap));
                    // Vérifie que a * u + b * v = GCD(a, p)
                    System.out.println("au + pv == pgcd(a,p) = "+gcd_ap.equals(bezout));
                    System.out.println("gcd == 1 = "+BigInteger.valueOf(1).equals(bezout)+"\n");
                }
                k++;
            }
            System.out.println("Les tests se trouvent dans le fichier test.txt \n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

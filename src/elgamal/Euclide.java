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
        BigInteger[] resultat = new BigInteger[3];
        if (b.equals(BigInteger.ZERO)) {
            resultat[0] = a;
            resultat[1] = BigInteger.ONE;
            resultat[2] = BigInteger.ZERO;
            return resultat;
        }
        BigInteger[] suiteResultats = euclideEtendu(b, a.remainder(b));

        BigInteger q = a.divide(b);
        BigInteger gcd = suiteResultats[0];
        BigInteger u = suiteResultats[2];

        BigInteger v = suiteResultats[1].subtract( q.multiply( suiteResultats[2]) );
        resultat[0] = gcd;
        resultat[1] = u;
        resultat[2] = v;

        return resultat;
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
                bufferedWriter.write("a = "+ a + "\t et ");
                BigInteger[] results = euclideEtendu(a, p);
                bufferedWriter.write("a.u + p.v = " + (a.multiply(results[1])).add(p.multiply(results[2])) + "\n");

                //sortie standard que pour les 10 premieres iterations
                if(k < 10){
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

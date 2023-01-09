package elgamal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class ExponentiationModulaire {

    private final BufferedWriter bufferedWriter;

    public ExponentiationModulaire(BufferedWriter bufferedWriter){
        this.bufferedWriter = bufferedWriter;
    }

    /**
     * Implementation exponentiation modulaire par la méthode d'exponentiation binaire
     * (récursif)
     * @param p grand entier modulo
     * @param g grand entier base
     * @param a grand entier exponent
     * @return A = (g^a) mod p
     */
    public BigInteger expMod(BigInteger p, BigInteger g, BigInteger a){
        BigInteger x = BigInteger.ONE;

        while(a.compareTo(BigInteger.ZERO) > 0) {   //tant que a>0
            if(a.testBit(0)) {  //a pair
                x = (x.multiply(g)).mod(p);
            }

            g = (g.multiply(g)).mod(p); //g = g**2 mod p
            a = a.shiftRight(1);    //a/=2
        }
        return x;
    }

    public void test10000Times(BigInteger p, BigInteger g) throws NoSuchAlgorithmException, NoSuchProviderException {
        BigInteger a;
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        System.out.println("Test de la fonction expMod() : ");

        try{
            bufferedWriter.write("Test de la fonction expMod() : \n");
            int k = 0;
            while(k<10000){
                a = new BigInteger(500,sr);
                bufferedWriter.write("a = "+a+"\t et ");
                bufferedWriter.write("expMod(p,g,a) = "+expMod(p,g,a)+"\n");

                if(k<5){
                    System.out.println("a = "+a+"\t et ");
                    System.out.println("expMod(p,g,a) = "+expMod(p,g,a));
                    System.out.println("((a^g) mod p == expMod(p,g,a)) = "+expMod(p,g,a).equals(g.modPow(a,p))+"\n");
                }

                k++;
            }
            System.out.println("Les tests se trouvent dans le fichier test.txt \n");
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}

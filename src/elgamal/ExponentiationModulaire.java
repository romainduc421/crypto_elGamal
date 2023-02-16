package elgamal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
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

    public void test10000Times(BigInteger p, BigInteger g, SecureRandom sr) {
        BigInteger a, ourExpMod, modPowofBigint;
        StringBuilder sb = new StringBuilder();

        sb.append("Test de la fonction expMod() : \n");
        for(int k=0; k<10000; k++){
            a = new BigInteger(500,sr);
            ourExpMod = expMod(p,g,a);
            modPowofBigint = g.modPow(a,p) ;
            assert(ourExpMod.equals(modPowofBigint)):"((a^g) mod p != expMod(p,g,a))";

            //5 premieres occurrences
            if(k<5){
                sb.append("a = ").append(a).append("\t et \n");
                sb.append("expMod(p,g,a) = ").append(ourExpMod).append("\n");
                sb.append("((a^g) mod p == expMod(p,g,a)) = ").append(ourExpMod.equals(modPowofBigint)).append("\n\n");
            }

        }
        try{
            bufferedWriter.write(sb.toString());
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(sb);
        System.out.flush();

    }
}

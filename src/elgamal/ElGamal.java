package elgamal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class ElGamal {
    private final ExponentiationModulaire exponentiationModulaire;
    private final BufferedWriter bufferedWriter;
    public ElGamal(BufferedWriter bufferedWriter){
        this.bufferedWriter = bufferedWriter;
        this.exponentiationModulaire = new ExponentiationModulaire(bufferedWriter);
    }

    /**
     * Generation des cles
     * @param p valeur du grand nombre premier
     * @param g valeur du grand nombre premier
     * @return BigInteger[2] contenant k_p et k_s
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public BigInteger[] keyGen(BigInteger p, BigInteger g) throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG", "SUN");
        BigInteger x = new BigInteger(1024, rand); // tire x au hasard (private-key)
        BigInteger X = exponentiationModulaire.expMod(p,g,x); // calcule X = g^x mod p (public-key)
        return new BigInteger[] {x, X};
    }

    /**
     * Fonction de chiffrement : prend en entree
     * la cle publique de Bob K_p = (p,g,X) et un message m
     * @param p
     * @param g
     * @param publicKey
     * @param m
     * @return couple chiffre {C,B} avec C = m.y mod p et B = g^r mod p
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public BigInteger[] encrypt(BigInteger p, BigInteger g, BigInteger publicKey, BigInteger m) throws NoSuchAlgorithmException, NoSuchProviderException{
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG", "SUN");
        BigInteger r = new BigInteger(1024, rand);
        BigInteger B = exponentiationModulaire.expMod(p, g, r); // calcule B = g^r mod p
        BigInteger C = m.multiply(exponentiationModulaire.expMod(p, publicKey, r)).mod(p); // calcule C = m * X^r mod p
        return new BigInteger[]{ C, B };
    }

    /**
     * Fonction de dechiffrement
     * @param C 1e partie du chiffre
     * @param B 2e partie du chiffre
     * @param x cle secrete de Bob
     * @param p grand entier : modulo
     * @return
     */
    public BigInteger decrypt(BigInteger C, BigInteger B, BigInteger x, BigInteger p) throws ArithmeticException {

        return (exponentiationModulaire.expMod(p,B,x).modInverse(p)).multiply(C).mod(p);
    }

    public void test100Times(BigInteger p, BigInteger g) throws NoSuchProviderException, NoSuchAlgorithmException{
        BigInteger message;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

        System.out.println("Test du chiffrement ElGamal : \n");
        try {
            bufferedWriter.write("Test du chiffrement ElGamal  : \n");
            int k = 0;
            while(k<100){
                message = BigInteger.valueOf(Math.abs(random.nextInt()));
                BigInteger[] keys = keyGen(p,g);
                BigInteger bobPrivateKey = keys[0];
                BigInteger bobPublicKey = keys[1];

                BigInteger[] encrypt = encrypt(p,g, bobPublicKey, message);
                BigInteger messageChiffreC = encrypt[0];
                BigInteger messageChiffreB = encrypt[1];

                bufferedWriter.write("Le message est : " + message.intValue() + "\n");
                bufferedWriter.write("Le message chiffré est : C  : " + messageChiffreC.intValue() +  "   -   et B  : " + messageChiffreB.intValue() + "\n");
                message = decrypt(messageChiffreC,messageChiffreB, bobPrivateKey, p);
                bufferedWriter.write("Le message déchiffré est : " + message.intValue() + "\n\n");

                if(k < 5){
                    System.out.println("Le message est : " + message.intValue());
                    System.out.println("Le message chiffré est :  C  = " + messageChiffreC.intValue() +  "   -   et B  = " + messageChiffreB.intValue());
                    message = decrypt(messageChiffreC,messageChiffreB, bobPrivateKey, p);
                    System.out.println("Le message déchiffré est : " + message.intValue() + "\n");
                }
                k++;
            }
            System.out.println("L'ensemble des tests se trouvent dans le fichier test.txt \n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

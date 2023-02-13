package elgamal;

import elgamal.exceptions.EuclideException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
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
     */
    public BigInteger[] keyGen(BigInteger p, BigInteger g) throws NoSuchAlgorithmException {
        SecureRandom rand = SecureRandom.getInstanceStrong();
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
     */
    public BigInteger[] encrypt(BigInteger p, BigInteger g, BigInteger publicKey, BigInteger m) throws NoSuchAlgorithmException{
        SecureRandom rand = SecureRandom.getInstanceStrong();
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
    public BigInteger decrypt(BigInteger C, BigInteger B, BigInteger x, BigInteger p) throws ArithmeticException, EuclideException {
        return (Euclide.modInv(exponentiationModulaire.expMod(p,B,x),p)).multiply(C).mod(p);
        //return (exponentiationModulaire.expMod(p,B,x).modInverse(p)).multiply(C).mod(p);
    }

    public void test100Times(BigInteger p, BigInteger g) throws NoSuchAlgorithmException{

        //returns an instance of the strongest SecureRandom implementation available on each platform
        SecureRandom random = SecureRandom.getInstanceStrong();

        System.out.println("Test du chiffrement ElGamal : \n");
        try {
            bufferedWriter.write("Test du chiffrement ElGamal  : \n");
            int k = 0;
            while(k<100){
                BigInteger message = BigInteger.valueOf(Math.abs(random.nextInt()));
                BigInteger[] keys = keyGen(p,g);
                BigInteger bobPrivateKey = keys[0];
                BigInteger bobPublicKey = keys[1];

                BigInteger[] encrypt = encrypt(p,g, bobPublicKey, message);
                BigInteger messageChiffreC = encrypt[0];
                BigInteger messageChiffreB = encrypt[1];
                BigInteger message2 = decrypt(messageChiffreC,messageChiffreB, bobPrivateKey, p);
                assert(message.intValue() == message2.intValue()):"message déchiffré différent";

                //5 premieres occurrences
                if(k < 5){
                    bufferedWriter.write("Le message est : " + message.intValue() + "\n");
                    bufferedWriter.write("Le message chiffré est : C  : " + messageChiffreC.intValue() +  "   -   et B  : " + messageChiffreB.intValue() + "\n");

                    bufferedWriter.write("Le message déchiffré est : " + message2.intValue()+"\n");
                    bufferedWriter.write("Message correctement déchiffré ?  " + (message.intValue() == message2.intValue()) + "\n\n");
                    System.out.println("Le message est : " + message.intValue());
                    System.out.println("Le message chiffré est :  C  = " + messageChiffreC.intValue() +  "   -   et B  = " + messageChiffreB.intValue());
                    System.out.println("Le message déchiffré est : " + message2.intValue());
                    System.out.println("Message correctement déchiffré ?  " + (message.intValue() == message2.intValue()) + "\n");
                }
                k++;
            }
            System.out.println("L'ensemble des tests se trouvent dans le fichier test.txt \n");

        } catch (IOException | EuclideException e) {
            e.printStackTrace();
        }
    }


    public void homomorphic_property(BigInteger p, BigInteger g) throws NoSuchAlgorithmException, EuclideException{
        BigInteger message, message2;
        //returns an instance of the strongest SecureRandom implementation available on each platform
        SecureRandom random = SecureRandom.getInstanceStrong();

        System.out.println("Test de la propriété homomorphe : \n");
        try {
            bufferedWriter.write("Test de la propriété homomorphe : \n");
            int k = 0;
            while(k<50){
                message = BigInteger.valueOf(Math.abs(random.nextInt()));
                message2 = BigInteger.valueOf(Math.abs(random.nextInt()));
                BigInteger[] keys = keyGen(p,g);
                BigInteger bobPrivateKey = keys[0];
                BigInteger bobPublicKey = keys[1];

                BigInteger[] encrypt = encrypt(p,g, bobPublicKey, message);
                BigInteger[] encrypt2 = encrypt(p,g, bobPublicKey, message2);
                
                BigInteger messageChiffreC = encrypt[0].multiply(encrypt2[0]).mod(p);
                BigInteger messageChiffreB = encrypt[1].multiply(encrypt2[1]).mod(p);

                BigInteger decryptTotal = decrypt(messageChiffreC,messageChiffreB, bobPrivateKey, p);
                BigInteger productm1m2 = message.multiply(message2).mod(p);

                assert(productm1m2.equals(decryptTotal)):"property not checked";
                //5 premieres occurrences
                if(k < 5){
                    System.out.println("m1 = "+message+", \tm2 = "+message2);
                    System.out.println("C and B decrypting gives : " + decryptTotal);
                    System.out.println("(m1 * m2).mod(p) = " + productm1m2);
                    System.out.println("Homomorphic property is checked : "+productm1m2.equals(decryptTotal)+"\n");
                    bufferedWriter.write("m1 = "+message+"\tm2 = "+message2+"\n");
                    bufferedWriter.write("C and B decrypting gives : " + decryptTotal+"\n");
                    bufferedWriter.write("(m1 * m2).mod(p) = " + productm1m2+"\n");
                    bufferedWriter.write("Homomorphic property is checked : "+productm1m2.equals(decryptTotal)+"\n\n");
                    
                }
                k++;
            }
        } catch (IOException | EuclideException e) {
            e.printStackTrace();
        }

    }
}

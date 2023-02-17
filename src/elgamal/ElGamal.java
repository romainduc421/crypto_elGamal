package elgamal;

import elgamal.exceptions.EuclideException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class ElGamal {
    private final ExponentiationModulaire exponentiationModulaire;
    private final Euclide euclide;
    private final BufferedWriter bufferedWriter;
    public ElGamal(BufferedWriter bufferedWriter){
        this.bufferedWriter = bufferedWriter;
        this.euclide = new Euclide(null);
        this.exponentiationModulaire = new ExponentiationModulaire(null);
    }

    /**
     * Generation des cles
     * @param p valeur du grand nombre premier
     * @param g valeur du grand nombre premier
     * @return BigInteger[2] contenant k_p et k_s
     */
    public BigInteger[] keyGen(BigInteger p, BigInteger g, SecureRandom rand) {
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
     */
    public BigInteger[] encrypt(BigInteger p, BigInteger g, BigInteger publicKey, BigInteger m, SecureRandom rand){
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
    public BigInteger decrypt(BigInteger C, BigInteger B, BigInteger x, BigInteger p) throws EuclideException {
        return (euclide.modInv(exponentiationModulaire.expMod(p,B,x),p)).multiply(C).mod(p);
    }

    /**
     * Test de la fonction de chiffrement et de dechiffrement
     * @param p
     * @param g
     * @param random
     * @throws EuclideException
     */
    public void test100Times(BigInteger p, BigInteger g, SecureRandom random, boolean all) throws EuclideException {
        BigInteger message, message2;
        BigInteger[] keys, encrypt;
        StringBuilder sb1 = new StringBuilder();

        sb1.append("Test du chiffrement ElGamal  : \n\n");

        if(all) {
            for (int k = 0; k < 100; k++) {
                message = BigInteger.valueOf(Math.abs(random.nextInt()));
                keys = keyGen(p, g, random);

                encrypt = encrypt(p, g, keys[1], message, random);
                message2 = decrypt(encrypt[0], encrypt[1], keys[0], p);
                assert (message.intValue() == message2.intValue()) : "message déchiffré différent";

                //5 premieres occurrences
                if (k < 5) {
                    sb1.append("Le message est : ").append(message.intValue()).append("\n");
                    sb1.append("Le message chiffré est : C  : ").append(encrypt[0].intValue()).append("   -   et B  : ").append(encrypt[1].intValue()).append("\n");

                    sb1.append("Le message déchiffré est : ").append(message2.intValue()).append("\n");
                    sb1.append("Message correctement déchiffré ?  ").append(message.intValue() == message2.intValue()).append("\n\n");
                }
            }
        }else{
            for (int k = 0; k < 5; k++) {
                message = BigInteger.valueOf(Math.abs(random.nextInt()));
                keys = keyGen(p, g, random);
                encrypt = encrypt(p, g, keys[1], message, random);
                message2 = decrypt(encrypt[0], encrypt[1], keys[0], p);
                assert (message.intValue() == message2.intValue()) : "message déchiffré différent";

                sb1.append("Le message est : ").append(message.intValue()).append("\n");
                sb1.append("Le message chiffré est : C  : ").append(encrypt[0].intValue()).append("   -   et B  : ").append(encrypt[1].intValue()).append("\n");

                sb1.append("Le message déchiffré est : ").append(message2.intValue()).append("\n");
                sb1.append("Message correctement déchiffré ?  ").append(message.intValue() == message2.intValue()).append("\n\n");
            }
        }
        try {
            bufferedWriter.write(sb1.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(sb1);
        System.out.flush();
    }


    /**
     * Test de la propriete homomorphique d'El Gamal
     * le produit de deux chiffrés donne le produit des deux clairs
     * @param p
     * @param g
     * @throws EuclideException
     */
    public void homomorphic_property(BigInteger p, BigInteger g, SecureRandom random) throws EuclideException{
        BigInteger message, message2;
        BigInteger[] keys, encrypt, encrypt2;
        BigInteger decryptTotal, productm1m2;
        StringBuilder sb1 = new StringBuilder();

        sb1.append("Test de la propriété homomorphe : \n\n");
        for(int k=0;k<5;k++) {
            message = BigInteger.valueOf(Math.abs(random.nextInt()));
            message2 = BigInteger.valueOf(Math.abs(random.nextInt()));
            keys = keyGen(p, g, random);

            encrypt = encrypt(p, g, keys[1], message, random);
            encrypt2 = encrypt(p, g, keys[1], message2, random);

            decryptTotal = decrypt(encrypt[0].multiply(encrypt2[0]).mod(p), encrypt[1].multiply(encrypt2[1]).mod(p), keys[0], p);
            productm1m2 = message.multiply(message2).mod(p);

            assert (productm1m2.equals(decryptTotal)) : "homomorphic property not checked";

            sb1.append("m1 = ").append(message).append("\tm2 = ").append(message2).append("\n");
            sb1.append("C and B decrypting gives : ").append(decryptTotal).append("\n");
            sb1.append("(m1 * m2).mod(p) = ").append(productm1m2).append("\n");
            sb1.append("Homomorphic property is checked : ").append(productm1m2.equals(decryptTotal)).append("\n\n");
        }
        try {
            bufferedWriter.write(sb1.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb1);
        System.out.println("L'ensemble des tests se trouvent dans le fichier test.txt \n");
        System.out.flush();
    }
}

import elgamal.ElGamal;
import elgamal.Euclide;
import elgamal.ExponentiationModulaire;
import elgamal.exceptions.EuclideException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Main {
    public static void main(String[] args) {
        //key length : 256 bits
        String primeMod = "ffffffffffffffffc90fdaa22168c234c4c6628b80dc1cd129024e088a67cc74020bbea63b139b22514a08798e3404ddef9519b3cd3a431b302b0a6df25f14374fe1356d6d51c245e485B576625e7ec6f44c42e9a637ed6b0bff5cb6f406b7edee386bfb5a899fa5ae9f24117c4b1fe649286651ece65381ffffffffffffffff";
        //String primeMod2 = "b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de426e5dd8f6eddef00b465f38f509b2b18351064704fe75f012fa346c5e2c442d7c99eac79b2bc8a202c98327b96816cb8042698ed3734643c4c05164e739cb72fba24f6156b6f47a7300ef778c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323";

        if(args.length == 0){
            System.out.println("Usage : java -ea Main [-all]");
            return;
        }
        boolean testAll = false;
        if(args.length == 1){
            System.out.println("pas de test d'assert pour les 995 ou 9995 autres itérations");
        }else{
            testAll = args[1].equals("-all");
        }

        //convert an hexadecimal string into a BigInteger
        BigInteger p = new BigInteger(primeMod, 16), g = BigInteger.TWO;
        SecureRandom sr;
        //returns an instance of the strongest SecureRandom implementation available on each platform
        try {
            sr = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        final long startTime = System.nanoTime();
        try{
            File file = new File("test.txt");

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

            Euclide euclide = new Euclide(bufferedWriter);
            ExponentiationModulaire exponentiationModulaire = new ExponentiationModulaire(bufferedWriter);
            ElGamal elGamal = new ElGamal(bufferedWriter);
            euclide.test10000Times(p, sr, testAll);
            exponentiationModulaire.test10000Times(p,g,sr, testAll);
            elGamal.test100Times(p,g,sr, testAll);
            elGamal.homomorphic_property(p,g,sr);
            bufferedWriter.close();
            long elapsedTime = System.nanoTime() - startTime;
            System.out.println("Total execution time (s): "
                    + elapsedTime/1000000000);
        }catch(IOException | EuclideException e){
            e.printStackTrace();
        }
    }
}

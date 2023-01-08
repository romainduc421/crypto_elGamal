import elgamal.Euclide;
import elgamal.exceptions.EuclideException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Main {
    public static void main(String[] args) {
        //key length : 256 bits
        String hexa = "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE65FFFFFFFFFFFFFFFF";

        //convert an hexadecimal string into a BigInteger
        BigInteger p = new BigInteger(hexa, 16);
        try{
            File file = new File("test.txt");

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

            Euclide euclide = new Euclide(bufferedWriter);
            euclide.test10000Times(p);
            bufferedWriter.close();
        }catch(IOException | NoSuchAlgorithmException | NoSuchProviderException | EuclideException e){
            e.printStackTrace();
        }
    }
}
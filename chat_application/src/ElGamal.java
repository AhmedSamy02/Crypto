import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


public class ElGamal {

    BigInteger p;
    BigInteger alpha; // secret key--x
    BigInteger hash;
    BigInteger g;
 public   BigInteger y;
    BigInteger k;
    BigInteger r;
// public static void main(String[] args) throws NoSuchAlgorithmException {
//         // Define the prime number p and generator g
//         BigInteger p = new BigInteger("17");
//         BigInteger g = new BigInteger("5");
//         // BigInteger m = new BigInteger("2"); // Example message// yb

//         // Create an instance of the ElGamal class
//         ElGamal elGamal = new ElGamal(p, g);

//         // Generate a signature for a message
//         Pair<BigInteger, BigInteger> signature = elGamal.sign();
//         BigInteger r = signature.getFirst();
//         BigInteger s = signature.getSecond();

//         // Print the generated signature
//         System.out.println("Generated signature (r, s): (" + r + ", " + s + ")");

//         // Verify the signature
//         boolean isValid = elGamal.verify(r, s, elGamal.y);
//         System.out.println("Signature is valid: " + isValid);
//     }


    ElGamal(BigInteger p, BigInteger g) {
        this.p = p;
        this.g = g;
        Random rand = new Random();
        alpha = new BigInteger(p.bitLength() - 1, rand).mod(p.subtract(BigInteger.valueOf(2))).add(BigInteger.ONE);
        y = g.modPow(alpha, p);
                    System.out.println("alpha "+alpha);

    }

    static BigInteger generateRandomNumberWithGCD(BigInteger p) {
        Random rnd = new Random();
        BigInteger result;

        do {
            // Generate a random number between 0 and p-1
            result = new BigInteger(p.bitLength() - 1, rnd).mod(p);
        } while (!gcd(result, p.subtract(BigInteger.ONE)).equals(BigInteger.ONE));

        return result;
    }
void print_all(){
    System.out.println("p "+p);
    System.out.println("g "+g);
    System.out.println("alpha "+alpha);
    System.out.println("y "+y);
    System.out.println("k "+k);
    System.out.println("r "+r);
    System.out.println("hash "+hash);
}
    // Function to compute the GCD of two integers
    static BigInteger gcd(BigInteger a, BigInteger b) {
        while (!b.equals(BigInteger.ZERO)) {
            BigInteger temp = b;
            b = a.mod(b);
            a = temp;
        }
        return a;
    }
 public static BigInteger hashBigInteger(BigInteger number) throws NoSuchAlgorithmException {
        // Convert the BigInteger to a byte array
        byte[] numberBytes = number.toByteArray();

        // Compute the SHA-1 hash of the byte array
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = digest.digest(numberBytes);

        // Convert the hash byte array to a BigInteger
        return new BigInteger(1, hashBytes);
    }

public Pair<BigInteger, BigInteger> sign(BigInteger m ) throws NoSuchAlgorithmException {
        BigInteger s;
            System.out.println("ms1 "+m);
        do {
            k = generateRandomNumberWithGCD(p);
            System.out.println("k "+k);
            r = g.modPow(k, p);
                   System.out.println("r "+r);

            hash = hashBigInteger(m);
                   System.out.println("hash1"+hash);
            BigInteger modhash=hash.mod(p.subtract(BigInteger.ONE));
            BigInteger alphaTimesR = alpha.multiply(r).mod(p.subtract(BigInteger.ONE));
                        System.out.println("modInverse"+alphaTimesR);

            BigInteger modInverse = k.modInverse(p.subtract(BigInteger.ONE)).mod(p.subtract(BigInteger.ONE));
                        System.out.println("modInverse"+modInverse);

            s = (modhash.subtract(alphaTimesR)).multiply(modInverse).mod(p.subtract(BigInteger.ONE));
        } while (s.equals(BigInteger.ZERO));
        System.out.println("s"+s);
        return new Pair<BigInteger, BigInteger>(r, s);
    }

  static  public boolean verify(BigInteger r, BigInteger s,BigInteger m,BigInteger p,BigInteger g,BigInteger yb)throws NoSuchAlgorithmException{

        // if(r.compareTo(BigInteger.ZERO) > BigInteger.ZERO && r.compareTo(p) <BigInteger.ZERO && s.compareTo(BigInteger.ZERO) > BigInteger.ZERO && s.compareTo(p.subtract(BigInteger.ONE)) < BigInteger.ZERO)
       { 
            System.out.println("ms2 "+m);
       BigInteger   inMessage = hashBigInteger(m);
       System.out.println("hash"+inMessage);
        BigInteger v1 = g.modPow(inMessage, p);
        BigInteger v2 = yb.modPow(r, p).multiply(r.modPow(s, p)).mod(p);
                System.out.println("Generated signature (v1, v2): (" + v1 + ", " + v2+ ")");

        return v1.equals(v2);}
// return false;
    }

   
}

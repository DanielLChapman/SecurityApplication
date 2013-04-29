/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rusty-Mac
 */
import java.math.BigInteger;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import java.io.*;
import javax.imageio.*;

public class Security extends javax.swing.JFrame {

    //Instantiates variables needed for the code
    //output
    static String output = "";
    //checks for input
    int newKey = 0, aliceAlert = 0;
    //Initialized IV
    static long IV = 2;
    //Instantiate the key
    private static int[] key = new int[4];
    //STarts a new random
    Random rand = new Random();
    //The Nonces used as IVs
    long Ra1, Rb1;
    //String Bob
    String bob = "bob";

    /**
     * Creates new form Security
     */
    public Security() {
        initComponents();
    }

    //Calls the encipher algorithm after the CBC is implemented.
    private static long[] encode(final long v[]) {
        long[] a = v;
        //CBC code to XOR plaintext with IV
        a[0] = a[0] ^ IV;
        a[1] = a[1] ^ IV;
        a = encipher(a);
        //Sets IV equal to the cipher text
        IV = a[0];
        return a;
    }

    //method to run the decipher algorithm , not needed but implemented for consistency.
    private static long[] decode(final long v[]) {
        long[] a = v;
        a = decipher(a);
        return a;
    }

    //Method to convert a string to a string hex.
    public static String toHex(String arg) {
        //Returns a formated string of %x (for hex) and uses BitInteger for conversion.
        return String.format("%x",
                new BigInteger(arg.getBytes(/* YOUR_CHARSET? */)));
    }

    //Method to convert a hex string to a string.
    public static String fromHex(String hex) {
        //creates a new string builder.
        StringBuilder output = new StringBuilder();
        //loops for the length of hex.
        for (int i = 0; i < hex.length(); i += 2) {
            //removes the first two character.
            String str = hex.substring(i, i + 2);
            //Uses the Integer method to convert the two characters to string from hex.
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    private String encodingAlgorithm(String inputString, long IV2) {
        //Resets the output
        output = "";
        //Grabs the IV
        IV = IV2;
        //Grabs the text
        String test = inputString;
        //Sets up a loop on the incoming text until the text is gone.
        while (test.length() > 1) {
            //Creates a string of arrays size 2 which will be the Left and Right.
            String[] in = new String[2];
            //Starts a for loop to fill the two string arrays
            for (int x = 0; x < 2; x++) {
                //initializes the array space
                in[x] = "";
                //creates a for loop that will take in the first 4 characters of the text and remove them.
                for (int i = 0; i < 4; i++) {
                    //try catch block in there to catch for remainders of the string with less then 4 characters
                    try {
                        in[x] = in[x] + test.charAt(0);
                        test = test.substring(1);
                    } catch (Exception ie) {
                    }
                }
                //Prints out the array of 4 characters
                System.out.print(in[x] + " ");
                //If in[0] or in[1] is empty, fill its with 4 spaces.
                if (in[x].isEmpty()) {
                    in[x] = "    ";
                }
                //Converts the two strings into hex to be able to convert the string to long.
                in[x] = toHex(in[x]);
                in[x] = toHex(in[x]);
                //prints out the new hex string.
                System.out.print(in[x] + " ");
            }
            //creates a new long array of size 2
            long[] a = new long[2];
            //fills the two arrays with the parse of the hex characters from before.
            a[0] = Long.parseLong(in[0]);
            a[1] = Long.parseLong(in[1]);
            //runs the encode algorithm on array a.
            a = encode(a);
            //fills the string array in with the parsed longs.
            in[0] = Long.toString(a[0]);
            in[1] = Long.toString(a[1]);
            //prints out the encrypted hex values for confirmation.
            System.out.println(in[0] + " " + in[1]);
            //sets output equal to 4 encrypted chars + * + 4 encrypted chars + * so that when it will
            //be decrypted, the strings can be parsed in regardless of length.
            output = output + in[0] + "*" + in[1] + "*";
            //commented section goes here
        }
        //attachs a / to the end of the encrypted file and sets the outputBox to that string.
        output = output + "/";
        return output;
    }

    private String decodingAlgorithm(String inputString, long IV3) {
        //Rsets output
        output = "";
        //sets IV = to IV3
        IV = IV3;
        //Grabs the input string
        String test = inputString;
        //Loops until the input is empty.
        while (test.length() > 1) {
            //creates a new string array
            String[] in = new String[2];
            //creates a for loop to fill the array
            for (int x = 0; x < 2; x++) {
                in[x] = "";
                //Checks to see if the input is * or / in which case it skips it.
                String c = String.valueOf(test.charAt(0));
                while (!c.equals("*")) {
                    c = String.valueOf(test.charAt(0));
                    if (c.equals("/") || c.equals("*")) {
                        test = test.substring(1);
                        break;
                    }
                    try {
                        //Takes in the first character, then removes it from the input.
                        in[x] = in[x] + test.charAt(0);
                        test = test.substring(1);
                    } catch (Exception ie) {
                    }
                }
                //Replaces an empty array as 2 are needed for the algorithm.
                if (in[x].isEmpty()) {
                    in[x] = "    ";
                }
                System.out.print(in[x] + " ");
            }
            //creates a 2 length long array which will be used in the decoding algorithm.
            long[] a = new long[2];
            //fills them
            a[0] = Long.parseLong(in[0]);
            a[1] = Long.parseLong(in[1]);
            //sets a blank long to the current cipher text
            long IV2 = a[0];
            //decodes the array
            a = decode(a);
            //Xors the data with the IV
            a[0] = a[0] ^ IV;
            a[1] = a[1] ^ IV;
            //Sets the IV equal to IV2 from before
            IV = IV2;
            //Converts the long to a string
            in[0] = Long.toString(a[0]);
            in[1] = Long.toString(a[1]);
            //Converts the hex string to hex string then to string.
            in[0] = fromHex(in[0]);
            in[0] = fromHex(in[0]);
            in[1] = fromHex(in[1]);
            in[1] = fromHex(in[1]);
            //Append it to the ouput
            output = output + in[0] + in[1];
            //Print it out for testing
            System.out.println(output);
            //commented section goes here
        }

        return output;

    }
    //Encipher Algorithm

    private static long[] encipher(final long v[]) {
        //Instantiate the delta value, th sum, L and R of the cipher and A and B but those arent needed.
        int delta = 0x9e3779B9;
        int sum = 0;
        long a = v[0], b = v[1];
        long L = a;// ^ IV1;
        long R = b;// ^ IV2;
        //Loops for 32 cycles.
        for (int i = 1; i <= 32; i++) {
            //Adds sum to delta.
            sum += delta;
            //Creates the L and R values
            L += ((R << 4) + key[0]) ^ (R + sum) ^ ((R >> 5) + key[1]);
            R += ((L << 4) + key[2]) ^ (L + sum) ^ ((L >> 5) + key[3]);
        }
        //Stores the values and return the 2 length array.
        v[0] = L;
        v[1] = R;
        return v;
    }

    //Algorithm for deciphering
    private static long[] decipher(final long v[]) {
        //Instantiates necessary variables
        int delta = 0x9e3779b9;
        int sum = delta << 5;
        int x = 0;
        //Begins a loop over hte length of the input, from a previous implementation, does nothing currently.
        while (x < v.length) {
            //Establishes the L and R values
            long L = v[x];
            long R = v[x + 1];
            // int i1 = L, i2 = R;
            //Loops 32 cycles to create the new L R and Sum data
            for (int i = 1; i <= 32; i++) {
                R -= ((L << 4) + key[2]) ^ (L + sum) ^ ((L >> 5) + key[3]);
                L -= ((R << 4) + key[0]) ^ (R + sum) ^ ((R >> 5) + key[1]);
                sum -= delta;
            }
            //Stores the values
            v[x] = L;// ^ IV1;
            v[x + 1] = R;// ^ IV2;
            // IV1 = v[x];
            // IV2 = v[x+1];
            //Was used in the loop in old implementation
            x += 2;
        }
        //Returns the value.
        return v;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sent = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        AliceAuth = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Alert = new javax.swing.JTextField();
        Go = new javax.swing.JToggleButton();
        Ra = new javax.swing.JTextField();
        Rb = new javax.swing.JTextField();
        bobsMessage = new javax.swing.JTextField();
        key1 = new javax.swing.JTextField();
        key0 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        newKeys = new javax.swing.JToggleButton();
        confirmBob = new javax.swing.JLabel();
        key3 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        key2 = new javax.swing.JTextField();
        sent2 = new javax.swing.JLabel();
        newKeyLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sent.setText("jLabel4");

        jLabel1.setText("Alice");

        AliceAuth.setText("Authenticated = ");

        jLabel2.setText("Bob");

        Alert.setText("Input");

        Go.setText("Send to Bob");
        Go.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GoActionPerformed(evt);
            }
        });

        Ra.setEditable(false);
        Ra.setText("*");

        Rb.setEditable(false);
        Rb.setText("*");

        bobsMessage.setText("Bobs Message");

        key1.setText("jTextField2");

        key0.setText("jTextField1");

        jButton1.setText("Decrypt");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        newKeys.setText("New Keys");
        newKeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newKeysActionPerformed(evt);
            }
        });

        confirmBob.setText("Confirm Bob = ");

        key3.setText("jTextField4");

        jButton2.setText("Authorize");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        key2.setText("jTextField3");

        newKeyLabel.setText("jLabel3");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(newKeys, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                    .add(key3)
                    .add(key0)
                    .add(key1)
                    .add(key2))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(53, 53, 53)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(Rb)
                                    .add(Alert))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(layout.createSequentialGroup()
                                        .add(Go, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 239, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(Ra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(bobsMessage)))
                            .add(layout.createSequentialGroup()
                                .add(6, 6, 6)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(layout.createSequentialGroup()
                                        .add(confirmBob, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 147, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(26, 26, 26)
                                        .add(jButton1)))))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(10, 10, 10)
                                .add(sent2))
                            .add(layout.createSequentialGroup()
                                .add(44, 44, 44)
                                .add(sent)))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel2)
                        .add(118, 118, 118))))
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(158, 158, 158)
                        .add(AliceAuth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 343, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(newKeyLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 647, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(key0, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(key1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(Alert, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(Go)
                    .add(Ra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sent))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(key2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(Rb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(bobsMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(key3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1)
                    .add(confirmBob))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(newKeys)
                    .add(jButton2)
                    .add(sent2))
                .add(18, 18, 18)
                .add(AliceAuth)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 31, Short.MAX_VALUE)
                .add(newKeyLabel)
                .add(44, 44, 44))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GoActionPerformed
        String message = " ";
        if (newKey == 0) {
            newKeyLabel.setText("Please generate new keys");
        } else {
            String alertText = Alert.getText();
            Ra1 = rand.nextInt(100);
            Ra.setText(Long.toString(Ra1));
            sent.setText("sent");
            File img = new File("bob.png");
            //Convert it to buffered image
            String encoded = " ";
            encoded = encodingAlgorithm(bob, Ra1);
            try {
                BufferedImage in = ImageIO.read(img);
                placeText(in, encoded);
                byte[] array;
                array = getData(in);
                byte[] array2;
                array2 = messageSize(array);
                array = getText(array, array2);
                message = (new String(array));
            } catch (Exception ie) {
            }
            bobsMessage.setText(message);
            Rb1 = rand.nextInt(100);
            Rb.setText(Long.toString(Rb1));
            aliceAlert = 1;
        }
    }//GEN-LAST:event_GoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (aliceAlert == 0) {
            newKeyLabel.setText("Alice needs to start");
        } else {
            String bobsMessage2 = bobsMessage.getText();
            String bobsOutput = decodingAlgorithm(bobsMessage2, Ra1);
            bobsMessage.setText(bobsOutput);
            System.out.println(bobsOutput);
            if (bobsOutput.compareTo("bob    ") == 0) {
                confirmBob.setText("confirmed bob = true");
            } else {
                confirmBob.setText("not bob " + bobsOutput + " " + bob);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String message = " ";
        if (Rb.getText().compareTo("*") == 0) {
            newKeyLabel.setText("Bob needs to respond");
        } else {
            String password = "Alice";
            String encoded = encodingAlgorithm(password, Ra1);
            File img2 = new File("password.png");
            File img3 = new File("alice.png");
            try {
                BufferedImage in2 = ImageIO.read(img2);
                BufferedImage in3 = ImageIO.read(img3);
                placeText(in2, encoded);
                byte[] array;
                array = getData(in2);
                byte[] array2;
                array2 = messageSize(array);
                array = getText(array, array2);
                password = decodingAlgorithm(new String(array), Ra1);
                encoded = encodingAlgorithm(password, Rb1);
                placeText(in3, encoded);
                array = getData(in3);
                array2 = messageSize(array);
                array = getText(array, array2);
                encoded = new String(array);
            } catch (Exception ie) {
            }
            sent2.setText("Message sent to bob");
            encoded = decodingAlgorithm(encoded, Rb1);
            System.out.println(encoded);
            if (encoded.compareTo("Alice") == 0) {
                AliceAuth.setText("Alice Authenticated = True");
            } else {
                AliceAuth.setText("Alice Authenticated = False");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void newKeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newKeysActionPerformed
        int[] keyCollection = new int[8];
        keyCollection[0] = 0xA56BABCD;
        keyCollection[1] = 0x00000000;
        keyCollection[2] = 0xFFFFFFFF;
        keyCollection[3] = 0xABCDEF01;
        keyCollection[4] = 0x1234ABCD;
        keyCollection[5] = 0x00FDAA34;
        keyCollection[6] = 0xFABC2593;
        keyCollection[7] = 0xCD123456;
        for (int i = 0; i < 4; i++) {
            int keys = rand.nextInt(8);
            key[i] = keyCollection[keys];
        }
        key0.setText(Integer.toString(key[0]));
        key1.setText(Integer.toString(key[1]));
        key2.setText(Integer.toString(key[2]));
        key3.setText(Integer.toString(key[3]));
        newKey = 1;
    }//GEN-LAST:event_newKeysActionPerformed

    private byte[] getData(BufferedImage image) {
        //returns an array of bytes
        //getRaster gets hte pixel information, Databuffer gets them all into one array, getdata gets the bytes. 
        return ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    }

    private byte[] textByte(String input) {
        //gets the message bytes
        byte[] message = input.getBytes();
        //gets the bytes of the message length
        byte[] lengthMessage = bitConversion(message.length);
        //gets the total length of both
        int total = lengthMessage.length + message.length;
        //sets a new array equal to the size of those two array lengths
        byte[] messageBytes = new byte[total];
        //copies the array length message to the message bytes
        //Then copies the message to messageBytes after the lengthMessage
        System.arraycopy(lengthMessage, 0, messageBytes, 0, lengthMessage.length);
        System.arraycopy(message, 0, messageBytes, lengthMessage.length, message.length);
        //returns the final array
        return messageBytes;
    }

    private BufferedImage placeText(BufferedImage imageBuffer, String txt) {
        //convert all data to arrays of bytes
        byte imageBytes[] = getData(imageBuffer);
        int offset = 0;
        byte text[] = textByte(txt);
        //loops through the bytes to put in
        for (int i = 0; i < text.length; i++) {
            for (int bit = 7; bit >= 0; offset++) //loops through the 8 bits of a byte
            {
                //ANDing imageBytes and 0xFE gives XXXXXXX0 as byte info, then ORs with the text shifted over so its either 00000000 or 00000001
                imageBytes[offset] = (byte) ((imageBytes[offset] & 0xFE) | (text[i] >>> bit));
                bit--;
            }
        }
        return imageBuffer;
    }

    private void saveImage(BufferedImage imageBuffer, File file) {
        try {
            //delete the old file and writes a new one
            file.delete();
            ImageIO.write(imageBuffer, "png", file);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private byte[] bitConversion(int i) {
        //only using 4 bytes
        byte[] bytes = new byte[4];
        //establishes the four offsets used
        int[] offsets = {0xFF0000, 0x00FF0000, 0x0000FF00, 0x000000FF};
        for (int x = 0; x < 4; x++) {
            //converts the ints to bytes. 
            bytes[x] = (byte) ((i & offsets[x]) >>> 24 - (x * 8));
        }
        return (bytes);
    }

    private byte[] messageSize(byte[] image) {
        int messageSize = 0;
        //loops through the first 32 bits of the message where we stored hte length of the message.
        for (int i = 0; i < 32; i++) {
            //message size gets shifted left 1, then ored with 00000000 or 00000001 which will increment the message size
            //since messageSize was stored as an array of bits it is returned as an array of bits.
            messageSize = (messageSize << 1) | (image[i] & 1);
        }
        byte[] results = new byte[messageSize];
        return results;
    }

    private byte[] getText(byte[] image, byte[] result) {
        int offset = 32;
        //loop through each byte of text
        for (int b = 0; b < result.length; ++b) {
            //loop through each bit within a byte
            for (int i = 0; i < 8; i++) {
                //shift result over 1, and or it with image[offset] & 1 
                //this will build the byte and then it moves to the next one.
                result[b] = (byte) ((result[b] << 1) | (image[offset] & 1));
                offset++;
            }
        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Security.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Security.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Security.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Security.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Security().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Alert;
    private javax.swing.JLabel AliceAuth;
    private javax.swing.JToggleButton Go;
    private javax.swing.JTextField Ra;
    private javax.swing.JTextField Rb;
    private javax.swing.JTextField bobsMessage;
    private javax.swing.JLabel confirmBob;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField key0;
    private javax.swing.JTextField key1;
    private javax.swing.JTextField key2;
    private javax.swing.JTextField key3;
    private javax.swing.JLabel newKeyLabel;
    private javax.swing.JToggleButton newKeys;
    private javax.swing.JLabel sent;
    private javax.swing.JLabel sent2;
    // End of variables declaration//GEN-END:variables
}

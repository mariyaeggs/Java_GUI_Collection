import java.lang.String;

/**
 * Programming Assignment 4: Optical Barcode Readers and Writers
 * School: CSU, Monterey Bay
 * Course: CST 338 Software Design
 * Professor: Jesse Cecil, MS
 *
 * This is an active, industrial, optical scanning and pattern recognition
 * program that simulates a 2D DataMatrix barcode.
 *
 * @author Mariya Eggensperger
 * @author Kenneth Vader
 * @author Brittany Mazza
 */
public class BarcodeScanner
{
   /**
    * Run example barcode reads.
    *
    * @param args command line arguments, which are not expected
    */
   public static void main(String args[])
   {
      String[] sImageIn =
         {
               "                                               ",
               "                                               ",
               "                                               ",
               "     * * * * * * * * * * * * * * * * * * * * * ",
               "     *                                       * ",
               "     ****** **** ****** ******* ** *** *****   ",
               "     *     *    ****************************** ",
               "     * **    * *        **  *    * * *   *     ",
               "     *   *    *  *****    *   * *   *  **  *** ",
               "     *  **     * *** **   **  *    **  ***  *  ",
               "     ***  * **   **  *   ****    *  *  ** * ** ",
               "     *****  ***  *  * *   ** ** **  *   * *    ",
               "     ***************************************** ",
               "                                               ",
               "                                               ",
               "                                               "
         };

      String[] sImageIn_2 =
         {
               "                                          ",
               "                                          ",
               "* * * * * * * * * * * * * * * * * * *     ",
               "*                                    *    ",
               "**** *** **   ***** ****   *********      ",
               "* ************ ************ **********    ",
               "** *      *    *  * * *         * *       ",
               "***   *  *           * **    *      **    ",
               "* ** * *  *   * * * **  *   ***   ***     ",
               "* *           **    *****  *   **   **    ",
               "****  *  * *  * **  ** *   ** *  * *      ",
               "**************************************    ",
               "                                          ",
               "                                          ",
               "                                          ",
               "                                          "
         };

      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);

      // First secret message
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // Second secret message
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // Create your own message
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // Create message in a blank barcode image
      BarcodeImage myBC = new BarcodeImage();
      DataMatrix myDM = new DataMatrix(myBC);
      myDM.readText("It's dangerous to go alone.");
      myDM.generateImageFromText();
      myDM.displayTextToConsole();
      myDM.displayImageToConsole();

   }
}

/**
 * The class representation of the 2D dot-matrix pattern. The class is
 * primarily made up of simple methods to see information on the barcode
 * image, as well as some to update the information on the barcode image.
 */
class BarcodeImage implements Cloneable
{
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;

   private boolean[][] image_data;

   /**
    * Create a new barcode image. The image will initialize with a white
    * barcode image.
    */
   public BarcodeImage()
   {
      // Initialize the image data with all false values for the maximum
      // width and height of the image. Note that new boolean values will
      // default to false if not instructed otherwise.
      this.image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
   }

   /**
    * Converts a 1D array of Strings to the internal 2D array of booleans
    * that make up the barcode image. When the barcode image is obtained, it
    * will be placed into the lower left-hand corner of the greater 2D array.
    *
    * @param str_data the list of strings to convert to a 2D array of booleans
    */
   public BarcodeImage(String[] str_data)
   {
      // Create a new barcode image that's all set to false.
      this.image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      if (!checkSize(str_data))
      {
         // Use the default new image data set if there isn't string data.
         return;
      }
      // Get the position of the bottom left corner of the image..
      int[] imageXY = new int[] { 0, 0 };
      boolean bottomLeftFound = false;
      for (int i = str_data.length - 1; i >= 0; i--)
      {
         String row = str_data[i];
         for (int j = 0; row != null && j < row.length(); j++)
         {
            boolean isTrue = str_data[i].charAt(j) == '*';
            // When we've found the first true value at the bottom left, set
            // the pixel location and flag to keep track of it.
            if (!bottomLeftFound && isTrue)
            {
               imageXY[0] = j;
               imageXY[1] = i;
               bottomLeftFound = true;
            }
         }
      }
      // Fill image from bottom left corner up toward the top right.
      for (int i = imageXY[1]; i >= 0 ; i--)
      {
         for (int j = 0; j < MAX_WIDTH; j++)
         {
            int x = j + imageXY[0];
            int y = imageXY[1] - i;
            if (y < 0 || y >= str_data.length
                  || str_data[y] == null
                  || x >= str_data[y].length())
            {
               break;
            }
            int imageY = MAX_HEIGHT - i - 1;
            this.image_data[imageY][j] = str_data[y].charAt(x) == '*';
         }
      }
   }

   /**
    * Get the pixel value in a particular row and column. The pixel is black
    * when true is returned, white otherwise.
    *
    * @param row the of the pixel to get the value for
    * @param col the column of the pixel to get the value for
    * @return the value of the pixel at the requested point where black is
    * true and white is false and false when there is an error
    */
   boolean getPixel(int row, int col)
   {
      // Return non-negative row
      return row >= 0
            && row < MAX_HEIGHT 
            && col < MAX_WIDTH
            && col >= 0
            && this.image_data[row] != null
            && this.image_data[row][col];
   }

   /**
    * Set a particular pixel in the image to either true or false,
    * respectively indicating if the pixel should be black or white.
    *
    * @param row the row where the pixel is located
    * @param col the column where the pixel is located
    * @param value the value to set the pixel to where black is true and
    *              white is false
    * @return true if the pixel was set, false otherwise
    */
   boolean setPixel(int row, int col, boolean value)
   {
      if (row < 0 || col < 0 || row >= MAX_HEIGHT || col >= MAX_WIDTH)
      {
         // Return false when the pixel position is not valid.
         return false;
      }
      this.image_data[row][col] = value;
      return true;
   }

   /**
    * Check the size of the incoming data to ensure that it is within the
    * constraints that we would like.
    *
    * @param data the data that we would like to check the size of
    * @return true if the data meets the size constraints, false otherwise
    */
   private boolean checkSize(String[] data)
   {
      // Return non-null data
      return data != null // non null data
            && data.length > 0 // and non-negative data
            && data.length <= MAX_HEIGHT  // doesn't surpass the max height of the barcode
            && data[0] != null // non null array
            && data[0].length() > 0 // non negative array
            && data[0].length() <= MAX_WIDTH; // doesn't surpass the max width of the barcode 
   }

   /**
    * Display the barcode image in the console. This is useful for debugging
    * purposes, but not necessary for the functionality of the class.
    */
   public void displayToConsole()
   {
      for (boolean[] imageRow : this.image_data)
      {
         for (boolean pixel : imageRow)
         {
            System.out.print(pixel ? "*" : " ");
         }
         System.out.println();
      }
   }

   /**
    * Get a deep clone of this barcode image.
    *
    * @return a deep clone of the BarcodeImage
    */
   @Override
   public BarcodeImage clone() throws CloneNotSupportedException
   {
      BarcodeImage copy = (BarcodeImage)super.clone();
      copy.image_data = this.image_data.clone();
      // Clone each array within the top-level image data array individually.
      for (int i = 0; i < this.image_data.length; i++)
      {
         copy.image_data[i] = this.image_data[i].clone();
      }
      return copy;
   }
}

/**
 * Defines the I/O and basic methods of any barcode class which might
 * implement it.
 */
interface BarcodeIO
{
   /**
    * Store a copy of a barcode image. The implementing class may
    * store an exact clone of the image or a refined, cleaned and
    * processed image.
    *
    * @param bc a barcode image to store a copy of
    * @return true if the scan was successful, false otherwise
    */
   public boolean scan(BarcodeImage bc);

   /**
    * Accepts a text string that can later be used for encoding an image.
    *
    * @param text the text to be used to encode an image later
    * @return true if the text reading was successful, false otherwise
    */
   public boolean readText(String text);

   /**
    * Generate and store (on the class itself) an encoded image based on the
    * text within the class itself.
    *
    * @return true if was stored successfully, false othewise
    */
   public boolean generateImageFromText();

   /**
    * Generate and store (on the class itself) a text string (also on the
    * class itself) that is decoded from the barcode image on the class.
    *
    * @return true if the text was decoded and stored successfully, false
    * otherwise
    */
   public boolean translateImageToText();

   /**
    * Display the text string in the console.
    */
   public void displayTextToConsole();

   /**
    * Display the image in the console.
    */
   public void displayImageToConsole();
}

/**
 * Represents an extremely simplified data matrix data structure (does not
 * contain error correction or encoding). The data matrix has a 2D array
 * format and a left and bottom black "spine" as well as an alternating black
 * and white pattern at the right and top of the image.
 */
class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';

   private BarcodeImage image; 
   private String text;
   private int actualWidth; // actualWidth of variation in barcode image
   private int actualHeight; // actualHeight of variation in barcode image

   /**
    * Create a new data matrix. The data matrix will be an empty, but
    * non-null image with an associated text value of "undefined".
    */
   public DataMatrix()
   {
      this.image = new BarcodeImage(); // Set a new non-null image
      this.text = "undefined"; // Text value 
      this.actualHeight = 0; 
      this.actualWidth = 0;
   }

   /**
    * Create a new data matrix based on a particular barcode image. The data
    * matrix image will be set to the image defined, but will not have the
    * associated text upon creation.
    *
    * @param image the image to create a data matrix from
    */
   public DataMatrix(BarcodeImage image)
   {
      scan(image); // Set image to default
   }

   /**
    * Create a new data matrix based on a particular text string. The data
    * matrix will be set to have the text defined, but will not have the
    * associated image upon creation.
    *
    * @param text the text to create a data matrix from
    */
   public DataMatrix(String text)
   {
      readText(text); // Set text to default
   }

   /**
    * Store a particular set of text on this data matrix.
    *
    * @param text the text to store on this data matrix
    * @return true if successful, false otherwise
    */
   public boolean readText(String text)
   {
      if (text == null)
      {
         return false; 
      }
      this.text = text;
      return true;
   }

   /**
    * Store a barcode image within this data matrix. No translation is done
    * here.
    *
    * @param image the image to scan into this data matrix
    * @return true if the scan was successful, false otherwise
    */
   public boolean scan(BarcodeImage image)
   {
      try
      {
         this.image = image.clone();
         this.actualHeight = computeSignalHeight();
         this.actualWidth = computeSignalWidth();
         cleanImage(); // Call the clean image method
         return true;
      }
      catch (CloneNotSupportedException e)
      {
         return false;
      }
   }

   /**
    * Get the actual width of the data matrix.
    *
    * @return the actual width of the data matrix
    */
   public int getActualWidth()
   {
      // Computes this.actualWidth = computeSignalWidth();
      return this.actualWidth;
   }

   /**
    * Get the actual height of the data matrix.
    *
    * @return the actual height of the data matrix
    */
   public int getActualHeight()
   {
      // Computes this.actualHeight = computeSignalHeight();
      return this.actualHeight;
   }

   /**
    * Generate the text string based on the image set within this class. The
    * generated text string will be set on the class itself and not returned.
    * After this method is called, it's expected that this data matrix will
    * contained a fully-defined image and text that agree with each other.
    *
    * @return true if the image was generated and set on the class, false
    * otherwise
    */
   public boolean generateImageFromText()
   {
      if (this.text == null || this.image == null) // If text and image are null
      {
         return false; // Do not return text 
      }
      this.clearImage();
      this.generateLeftBorder();
      this.generateRightBorder();
      this.generateBottomBorder();
      this.generateTopBorder();
      int textLength = this.text.length();
      for (int i = 1; i <= textLength && i < BarcodeImage.MAX_WIDTH; i++)
      {
         writeCharToCol(i, (int) text.charAt(i - 1));
      }

      this.actualHeight = this.computeSignalHeight();
      this.actualWidth = this.computeSignalWidth();
      return true;
   }

   /**
    * Writes the binary representation of an individual character into given
    * column of image.
    *
    * @param col column to write into
    * @param code integer representation of char
    * @return true if successful, false otherwise
    */
   private boolean writeCharToCol(int col, int code)
   {
      if (col <= 0 || col >= BarcodeImage.MAX_WIDTH || code < 0 || code >= 128)
      {
         return false;
      }
      boolean[] binaryArr = intToBinaryArray(code);
      for (int i = 7; i >= 0; i--)
      {
         this.image.setPixel(i + 21, col, binaryArr[i]);
      }
      return true;
   }

   /**
    * Creates a boolean array of length 8, suitable for holding an 8-bit
    * binary number.
    *
    * @param num The integer to be converted to binary
    * @return a boolean array representing an 8 bit binary number
    */
   private boolean[] intToBinaryArray(int num)
   {
      boolean[] binaryArr = new boolean[8];
      if (num >= 0 && num < 128)
      {
         // Starting with the highest order bit, shift 1 bit i times, perform
         // bitwise AND with original number. If shifted bit was on for original
         // number, place true in binaryArr for that position, otherwise
         // place false.
         for (int i = 7; i >= 0; i--)
         {
            binaryArr[7 - i] = (num & (1 << i)) != 0;
         }
      }
      return binaryArr;
   }

   /**
    * Generate the image based on the text string set within this class. The
    * generated image will be set on the class itself and not returned.
    * After this method is called, it's expected that this data matrix will
    * contained a fully-defined image and text that agree with each other.
    */
   @Override public boolean translateImageToText()
   {
      if (this.image == null)
      {
         return false;
      }
      this.text = "";
      for (int i = 1; i < this.actualWidth - 1; i++)
      {
         char columnChar = readCharFromCol(i);
         this.text += Character.toString(columnChar);
      }
      // Remove the leading and trailing whitespace from the text message.
      this.text = this.text.trim();
      return true;
   }

   /**
    * Helper method to assist in translating image to text. Traverses given
    * column, adds up values that are true and returns as a char.
    *
    * @param col column to traverse
    * @return char represented in column of image
    */
   private char readCharFromCol(int col)
   {
      // Check for valid column and image
      if (col <= 0 || this.image == null)
      {
         // Return a new line character if the column and image are not valid.
         return '\n';
      }
      int sum = 0;
      // Loop runs from bottom up in a single column. Each time it encounters a
      // true value it uses the current row position to determine the
      // exponent value on a base of 2. Sum those values through the column,
      // return as a char.
      int heightDifference = BarcodeImage.MAX_HEIGHT - this.actualHeight;
      for (int i = BarcodeImage.MAX_HEIGHT - 2; i > heightDifference; i--)
      {
         if (this.image.getPixel(i, col))
         {
            sum += Math.pow(2, BarcodeImage.MAX_HEIGHT - 2 - i);
         }
      }
      return (char) sum;
   }

   /**
    * Display the text string for this data matrix in the console.
    */
   @Override public void displayTextToConsole()
   {
      System.out.println(this.text); 
   }

   /**
    * Display the cropped image to the console with a border.
    */
   @Override public void displayImageToConsole()
   {
      // Display top row border.
      for (int i = 0; i < this.actualWidth; i++)
      {
         System.out.print("-");
      }
      System.out.println("--");
      // Display data matrix image, trimmed.
      int startColumn = BarcodeImage.MAX_HEIGHT - this.actualHeight;

      for (int i = startColumn; i < BarcodeImage.MAX_HEIGHT; i++)
      {
         System.out.print("|");
         for (int j = 0; j < this.actualWidth + 1; j++)
         {
            boolean pixel = this.image.getPixel(i, j);
            System.out.print(pixel ? BLACK_CHAR : WHITE_CHAR);
         }
         System.out.println("|");
      }
   }

   /**
    * Display the full, raw image in the console (includes the blank top and
    * right).
    */
   public void displayRawImage()
   {
      this.image.displayToConsole();
   }

   /**
    * Clear the entire image by setting all pixels within the image to false.
    */
   private void clearImage()
   {
      int startColumn = BarcodeImage.MAX_HEIGHT - this.actualHeight;
      if (this.image == null)
      {
         // Return early if there isn't an image to clear.
         return;
      }
      for (int i = startColumn; i < BarcodeImage.MAX_HEIGHT; i++)
      {
         for (int j = 0; j <= this.actualWidth; j++)
         {
            // Clear image by setting pixel to false (or white).
            this.image.setPixel(i, j, false);
         }
      }
   }

   /**
    * Determine the actual width of the image within the correctly situated
    * larger image.
    *
    * @return the actual width of the data
    */
   private int computeSignalWidth()
   {
      if (this.image == null || !imageNotBlank())
      {
         return 0;
      }
      int leftCol = findBottomLeft()[1];
      int row = findBottomLeft()[0];
      int col = leftCol;
      // Iterate across bottom row until we no longer find true values
      while (this.image.getPixel(row, col))
      {
         if (col >= BarcodeImage.MAX_WIDTH - 1)
         {
            col = BarcodeImage.MAX_WIDTH - 1;
            break;
         }
         col++;
      }
      // The difference between col and leftCol gives width of actual image.
      return col - leftCol;
   }

   /**
    * Determine the actual width of the image within the correctly situated
    * larger image.
    *
    * @return the actual height of the data
    */
   private int computeSignalHeight()
   {
      if (this.image == null || !imageNotBlank())
      {
         return 0;
      }
      int topRow = findTopLeft()[0];
      int bottomRow = findBottomLeft()[0];
      return bottomRow - topRow + 1;
   }

   /**
    * Helper method to determine if image is blank (all false values).
    *
    * @return true if image is not blank, false if image is blank
    */
   private boolean imageNotBlank()
   {
      if (image != null)
      {
         // Return true if a true value is found to know if an image exists.
         for (int i = 0; i < BarcodeImage.MAX_HEIGHT; i++)
         {
            for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++)
            {
               if (this.image.getPixel(i, j))
               {
                  return true;
               }
            }
         }
      }
      return false;
   }

   /**
    * Clean the image. The image may be moved into the expected location for
    * this data matrix.
    */
   private void cleanImage()
   {
      if (this.image == null || this.text == null)
      {
         return;
      }
      moveImageToLowerLeft();
   }

   /**
    * Move the image into the corner at the bottom left.
    */
   private void moveImageToLowerLeft()
   {
      // Move image as far down as possible.
      int[] lowerLeftCorner = findBottomLeft();
      int heightOffset = (BarcodeImage.MAX_HEIGHT - 1) - lowerLeftCorner[0];
      if (heightOffset > 0)
      {
         shiftImageDown(heightOffset);
      }
      // Move image as far left as possible.
      int widthOffset = lowerLeftCorner[1];
      if (widthOffset > 0)
      {
         shiftImageLeft(widthOffset);
      }
   }

   /**
    * Move image lower in the 2D array based on the offset desired.
    *
    * @param offset the amount to move the image lower
    */
   private void shiftImageDown(int offset)
   {
      if (this.image == null)
      {
         // If image is null, return early.
         return;
      }
      // Iterate through image length.
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= 0; i--)
      {
         // Iterate through image width
         for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++)
         {
            // Move the image to desired offset.
            boolean tmpPixel = this.image.getPixel(i - offset, j);
            this.image.setPixel(i, j, tmpPixel);
         }
      }
   }

   /**
    * Move image left in the 2D array based on the offset desired.
    *
    * @param offset the amount to move the image to the left
    */
   private void shiftImageLeft(int offset)
   {
      if (this.image == null)
      {
         // If image is null, return early.
         return;
      }
      // Iterate through image length.
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= 0; i--)
      {
         // Iterate through image width.
         for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++)
         {
            // Shift image to the left.
            boolean tmpPixel = this.image.getPixel(i, j + offset);
            this.image.setPixel(i, j, tmpPixel);
         }
      }
   }

   /**
    * Iterates through a valid image object to find the x, y location
    * of the top left corner of the image.
    *
    * @return integer array of length two, in the form of arr[row, col]
    */
   private int[] findTopLeft()
   {
      int[] imageTop = new int[2];

      int row = 0, col = 0;
      // Check to make sure image points to a valid object.
      if (this.image != null)
      {
         // Find the top of the left hand solid bar.
         while(!this.image.getPixel(row, col))
         {
            if (col >= BarcodeImage.MAX_WIDTH - 1)
            {
               col = 0;
               row++;
            }
            else
            {
               col++;
            }
         }
         imageTop[0] = row;
         imageTop[1] = col;
      }

      return imageTop;
   }

   /**
    * Iterates through a valid image object to find the x,y location
    * of the bottom left corner of the image.
    *
    * @return integer array of length two, in the form of arr[row, col]
    */
   private int[] findBottomLeft()
   {
      int[] imageCorner = new int[2];

      if (this.image != null)
      {
         imageCorner = findTopLeft();
         int row = imageCorner[0];
         int col = imageCorner[1];
         // Find the bottom of the left hand solid bar
         while(this.image.getPixel(row, col))
         {
            if (row >= BarcodeImage.MAX_HEIGHT - 1)
            {
               row = BarcodeImage.MAX_HEIGHT - 1;
               break;
            }
            row++;
         }
         imageCorner[0] = row;
      }

      return imageCorner;
   }

   /**
    * Private method to set a border for the left side of the image.
    *
    * @return true if set successfully, false otherwise
    */
   private boolean generateLeftBorder()
   {
      if (this.image == null) // If image is null
      {
         return false; // Do not set a border
      }

      // Set image left image border.
      int imageHeight = BarcodeImage.MAX_HEIGHT - 10;
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= imageHeight; i--)
      {
         this.image.setPixel(i, 0, true);
      }
      return true;
   }

   /**
    * Private method to set a border for the right side of the image.
    *
    * @return true if set successfully, false otherwise
    */
   private boolean generateRightBorder()
   {
      if (this.image == null)
      {
         // If image is null, do not set a border.
         return false;
      }

      // Set right image border.
      int imageHeight = BarcodeImage.MAX_HEIGHT - 10;
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= imageHeight; i--)
      {
         if (i % 2 != 0)
         {
            this.image.setPixel(i, this.text.length() + 1, true);
         }
      }
      return true;
   }

   /**
    * Private method to set a border for the top of the image.
    *
    * @return true if set successfully, false otherwise
    */
   private boolean generateTopBorder()
   {
      if (this.image == null)
      {
         // If image is null, do not set a border
         return false;
      }

      // Set the top image border.
      for (int i = 0; i < this.text.length(); i++)
      {
         if (i % 2 == 0)
         {
            this.image.setPixel(BarcodeImage.MAX_HEIGHT - 10, i, true);
         }
      }

      return true;
   }

   /**
    * Private method to set a border for the bottom of the image.
    *
    * @return true if set successfully, false otherwise
    */
   private boolean generateBottomBorder()
   {
      if (this.image == null)
      {
         // If image is null, do not set a border
         return false;
      }

      // Set the bottom image border.
      for (int i = 0; i < this.text.length() + 1; i++)
      {
         this.image.setPixel(BarcodeImage.MAX_HEIGHT - 1, i, true);
      }

      return true;
   }
}
/*-----------------------------------------OUTPUT-------------------------------
 *

CSUMB CSIT online program is top notch.
-------------------------------------------
|* * * * * * * * * * * * * * * * * * * * * |
|*                                       * |
|****** **** ****** ******* ** *** *****   |
|*     *    ****************************** |
|* **    * *        **  *    * * *   *     |
|*   *    *  *****    *   * *   *  **  *** |
|*  **     * *** **   **  *    **  ***  *  |
|***  * **   **  *   ****    *  *  ** * ** |
|*****  ***  *  * *   ** ** **  *   * *    |
|***************************************** |
You did it!  Great work.  Celebrate.
----------------------------------------
|* * * * * * * * * * * * * * * * * * *  |
|*                                    * |
|**** *** **   ***** ****   *********   |
|* ************ ************ ********** |
|** *      *    *  * * *         * *    |
|***   *  *           * **    *      ** |
|* ** * *  *   * * * **  *   ***   ***  |
|* *           **    *****  *   **   ** |
|****  *  * *  * **  ** *   ** *  * *   |
|************************************** |
What a great resume builder this is!
----------------------------------------
|* * * * * * * * * * * * * * * * * *    |
|*                                    * |
|***** * ***** ****** ******* **** **   |
|* ************************************ |
|**  *    *  * * **    *    * *  *  *   |
|* *               *    **     **  *  * |
|**  *   * * *  * ***  * ***  *         |
|**      **    * *    *     *    *  * * |
|** *  * * **   *****  **  *    ** ***  |
|************************************** |
It's dangerous to go alone.
-------------------------------
|* * * * * * * * * * * * * *   |
|*                           * |
|*** * ********* ** ** *****   |
|* *************************** |
|* * *      * ** *             |
|**      *   *    *  *  *** ** |
|* **  * *** **  ** **  *****  |
|*  **   ** ** *  * **   ** ** |
|** **  * ** ***  * ** * * *   |
|***************************** |


 *ENDS OUTPUT/
*/

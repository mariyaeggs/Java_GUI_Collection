/**
 * Programming Assignment 2: Casino
 * School: CSU, Monterey Bay
 * Course: CST 338 Software Design
 * Professor: Jesse Cecil, MS
 *
 * Java slot machine simulator. Program loops through and prompts user to make
 * a bet. User is rewarded based on placed bet and slot outcome.
 *
 * @author Mariya Eggesnperger
 * @author Kenneth Vader
 * @author Brittany Mazza
 */
import java.lang.Math;
import java.util.Scanner;

public class SlotMachine
{
   private static Scanner scannerRecord = new Scanner(System.in);

   /**
    * Run the slot machine game. Enter "0" to quit the game at any time.
    *
    * @param args command line arguments, which are not expected
    */
   public static void main( String[] args )
   {
      int winning;
      // Get the users's initial bet and perform their first "pull".
      int userBet = getBet();
      TripleString pullResult = pull();

      // If the user enters "0" as their bet, the game will end. Continue
      // asking for bets and running the game until the user bets "0".
      while (userBet > 0)
      {
         // Based on the user's pull results, we get a pay multiplier that is
         // used to calculate their winnings for the pull (their bet
         // multiplied by the pay multiplier).
         winning = getPayMultiplier(pullResult) * userBet;
         if (!pullResult.saveWinnings(winning)) {
            // Exit the program if the winnings couldn't be saved (due to
            // exceeding max pulls).
            break;
         }
         display(pullResult, winning);
         // Get the user's bet again and perform the next slot machine pull.
         userBet = getBet();
         pullResult = pull();
      }

      // Thank the user for play and display their total winnings.
      System.out.println("Thanks for playing at the Casino!");
      System.out.println(pullResult.displayWinnings());

      // Close Scanner to avoid resource leaks.
      scannerRecord.close();
   }

   /**
    * Get the amount that the user would like to bet.
    *
    * @return the bet amount placed by the user
    */
   public static int getBet() {
      int userBet;

      do
      {
         System.out.print("\nHow much would you like to bet (1-100) or 0 to "
               + "quit? ");
         // Return bet amount as a functional return.
         userBet = scannerRecord.nextInt();
         // Gather the information again if the user bet surpasses parameters.
         if (userBet > 100 || userBet < 0)
         {
            System.out.println("Bet must be between 1 and 100.");
         }
      } while (userBet < 0 || userBet > 100);

      return userBet;
   }

   /**
    * Method produces and returns random string from a set of possible
    * predetermined strings based on required probabilities.
    *
    * The probability of getting each string getting returned is as follows:
    *   1. "BAR" has a 50% probability.
    *   2. "cherries" has a 25% probability.
    *   3. "space" has a 12.5% probability.
    *   4. "7" has a 12.5% probability.
    *
    * @return a random string of either "BAR", "cherries", "space", or "7"
    */
   public static String randString()
   {
      // Pick a random number out of 1000 possibilities (0-999, inclusive).
      int randomNumber = (int) (Math.random() * 1000);

      // Use the outcome of the random number to determine which string to
      // return. The ranges have been set to varying amounts to give certain
      // strings a higher probability than others. Note that the ranges are
      // inclusive.
      if (randomNumber >= 0 && randomNumber < 500)
      {
         // Range 0-499 (of 1000) has 50% probability (500 / 1000 = 50%).
         return "BAR";
      }
      else if (randomNumber < 750)
      {
         // Range 500-749 (of 1000) has 25% probability (250 / 1000 = 25%).
         return "cherries";
      }
      else if (randomNumber < 875)
      {
         // Range 750-874 (of 1000) has 12.5% probability (125 / 1000 = 12.5%).
         return "space";
      }
      else if (randomNumber < 1000)
      {
         // Range 875-999 (of 1000) has 12.5% probability (125 / 1000 = 12.5%).
         return "7";
      }

      return "";
   }

   /**
    * Method initiates and returns a TripleString object to the user.
    *
    * @return a new slot machine pull
    */
   public static TripleString pull()
   {
      TripleString pullString = new TripleString();

      // Generate three random strings and for a new slot machine pull and
      // return the pull outcome.
      pullString.setString1(randString());
      pullString.setString2(randString());
      pullString.setString3(randString());

      return pullString;
   }

   /**
    * Get the pay multiplier based on the user's recent slot machine pull
    * results to reward user with "pay" for the gamble (calculated by
    * multiplying the bet and the pay multiplier calculated here).
    *
    * There are several outcome possibilities that each provide different
    * values:
    *   1. $5 for "cherries" [not "cherries"] [any string]
    *   2. $15 for "cherries" "cherries" [not "cherries"]
    *   3. $30 for "cherries" "cherries" "cherries"
    *   4. $50 for "BAR" "BAR" "BAR"
    *   5. $100 for "7" "7" "7"
    *   6. $0 for any combination not listed above.
    *
    * @param thePull the recent slot machine pull performed by the user
    *
    * @return the amount that the user's bet will be multiplied by based on
    * their pull results
    */
   public static int getPayMultiplier( TripleString thePull )
   {
      String string1 = thePull.getString1();
      String string2 = thePull.getString2();
      String string3 = thePull.getString3();

      if (string1.equals("cherries") && !string2.equals("cherries"))
      {
         // Combination 1 is cherries [not cherries] [any value]
         return 5;
      }
      else if (string1.equals("cherries") && string2.equals("cherries")
            && !string3.equals("cherries"))
      {
         // Combination 2 is cherries cherries [not cherries]
         return 15;
      }
      else if (string1.equals("cherries") && string2.equals("cherries")
            && string3.equals("cherries"))
      {
         // Combination 3 is cherries cherries cherries
         return 30;
      }
      else if (string1.equals("BAR") && string2.equals("BAR")
            && string3.equals("BAR"))
      {
         // Combination 4 is BAR BAR BAR
         return 50;
      }
      else if (string1.equals("7") && string2.equals("7")
            && string3.equals("7"))
      {
         // Combination 5 is 7 7 7
         return 100;
      }

      return 0;
   }

   /**
    * Inform the user of their winnings from their most recent slot machine
    * pull. If they lost, let them know.
    *
    * @param thePull the most recent slot machine pull performed by the user
    * @param winnings the pay that the user received from their last slot
    *                 machine pull
    */
   public static void display( TripleString thePull, int winnings )
   {
      // Show the user their slot machine pull results. If they have won,
      // display how much they won.
      String pullMessage = "\nwhirrrrr...and your pull is...\n";
      String resultMessage = winnings == 0
            ? "\nSorry, you lose."
            : "\nCongratulations, you win: $" + winnings;
      System.out.println(pullMessage + thePull.toString() + resultMessage);
   }
}

/**
 * Stores three strings that represent the results from a slot machine pull
 * data. Additionally, keep track of all winnings from previous pulls
 * performed by the user.
 */
class TripleString
{
   public static final double MAX_LEN = 20;
   public static final int MAX_PULLS = 40;

   private String string1;
   private String string2;
   private String string3;
   private static int numPulls = 0;
   private static int[] pullWinnings = new int[MAX_PULLS];

   /**
    * Default constructor initializes all string members to "".
    */
   TripleString()
   {
      string1 = "";
      string2 = "";
      string3 = "";
   }

   /**
    * Determine whether a String is legal.
    *
    * @param str a string to ensure it has met the requirements and is legal
    *
    * @return if the string is valid or not
    */
   private boolean validString( String str )
   {
      // Return if the string is legal, which is when it exists, and meets
      // the length requirements.
      return str != null && str.length() <= MAX_LEN;
   }

   /**
    * Set the first string.
    *
    * @param str the string to set as the first string
    *
    * @return true if the string was set properly, false otherwise
    */
   public boolean setString1( String str )
   {
      if (!validString(str))
      {
         return false;
      }
      string1 = str;
      return true;
   }

   /**
    * Set the second string.
    *
    * @param str the string to set as the second string
    *
    * @return true if the string was set properly, false otherwise
    */
   public boolean setString2( String str )
   {
      if (!validString(str))
      {
         return false;
      }
      string2 = str;
      return true;
   }

   /**
    * Set the third string.
    *
    * @param str the string to set as the first string
    *
    * @return true if the string was set properly, false otherwise
    */
   public boolean setString3( String str )
   {
      if (!validString(str))
      {
         return false;
      }
      string3 = str;
      return true;
   }

   /**
    * Get the first string.
    *
    * @return the first string from TripleString
    */
   public String getString1()
   {
      return string1;
   }

   /**
    * Get the second string.
    *
    * @return the second string from TripleString
    */
   public String getString2()
   {
      return string2;
   }

   /**
    * Get the third string.
    *
    * @return the third string from TripleString
    */
   public String getString3()
   {
      return string3;
   }

   /**
    * Return all the strings as one string.
    *
    * @return the first, second, and third string from TripleString as one
    *         string
    */
   public String toString()
   {
      return string1 + "       " + string2 + "       " + string3;
   }

   /**
    * Update pullWinnings[] array with new winnings value.
    *
    * @param winnings the amount the user was paid by their most recent slot
    *                 machine pull
    *
    * @return true if the winnings were saved properly, false otherwise
    */
   public boolean saveWinnings( int winnings )
   {
      if (numPulls >= MAX_PULLS)
      {
         // Don't save winnings if the user has exceeded the number of
         // allotted pulls.
         return false;
      }
      // Save the new winnings and increment the number of total pulls the
      // user performed.
      pullWinnings[numPulls] = winnings;
      numPulls++;
      return true;
   }

   /**
    * Return a string that contains each individual win/loss for each pull
    * and total winnings for the game.
    *
    * @return the string that displays all of the accumulated winnings
    */
   public String displayWinnings()
   {
      String results = "Your individual winnings were:\n";
      int totalWinnings = 0;

      // Accumulate and display the individual and total winnings for the
      // user from all of their slot machine pulls.
      for (int i = 0; i < numPulls; i++)
      {
         results += pullWinnings[i] + " ";
         totalWinnings += pullWinnings[i];
      }

      return results + "\nYour total winnings were: $" + totalWinnings;
   }
}

/*********************************** OUTPUT ************************************

How much would you like to bet (1-100) or 0 to quit? 10003
Bet must be between 1 and 100.

How much would you like to bet (1-100) or 0 to quit? -10
Bet must be between 1 and 100.

How much would you like to bet (1-100) or 0 to quit? 1

whirrrrr...and your pull is...
BAR       cherries       7
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 2

whirrrrr...and your pull is...
BAR       cherries       BAR
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 3

whirrrrr...and your pull is...
BAR       BAR       cherries
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 4

whirrrrr...and your pull is...
cherries       7       7
Congratulations, you win: $20

How much would you like to bet (1-100) or 0 to quit? 5

whirrrrr...and your pull is...
BAR       BAR       BAR
Congratulations, you win: $250

How much would you like to bet (1-100) or 0 to quit? 6

whirrrrr...and your pull is...
BAR       cherries       7
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 7

whirrrrr...and your pull is...
cherries       space       BAR
Congratulations, you win: $35

How much would you like to bet (1-100) or 0 to quit? 8

whirrrrr...and your pull is...
space       cherries       BAR
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 9

whirrrrr...and your pull is...
cherries       cherries       7
Congratulations, you win: $135

How much would you like to bet (1-100) or 0 to quit? 10

whirrrrr...and your pull is...
BAR       BAR       cherries
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 11

whirrrrr...and your pull is...
7       space       BAR
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 12

whirrrrr...and your pull is...
7       cherries       BAR
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 13

whirrrrr...and your pull is...
space       cherries       cherries
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 14

whirrrrr...and your pull is...
7       space       BAR
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 15

whirrrrr...and your pull is...
7       BAR       BAR
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 16

whirrrrr...and your pull is...
BAR       BAR       7
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 17

whirrrrr...and your pull is...
BAR       BAR       cherries
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 18

whirrrrr...and your pull is...
BAR       space       BAR
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 19

whirrrrr...and your pull is...
cherries       BAR       7
Congratulations, you win: $95

How much would you like to bet (1-100) or 0 to quit? 20

whirrrrr...and your pull is...
cherries       BAR       cherries
Congratulations, you win: $100

How much would you like to bet (1-100) or 0 to quit? 21

whirrrrr...and your pull is...
space       BAR       cherries
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 22

whirrrrr...and your pull is...
space       cherries       BAR
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 23

whirrrrr...and your pull is...
7       cherries       7
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 24

whirrrrr...and your pull is...
cherries       cherries       BAR
Congratulations, you win: $360

How much would you like to bet (1-100) or 0 to quit? 25

whirrrrr...and your pull is...
BAR       BAR       BAR
Congratulations, you win: $1250

How much would you like to bet (1-100) or 0 to quit? 26

whirrrrr...and your pull is...
7       BAR       cherries
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 27

whirrrrr...and your pull is...
BAR       BAR       BAR
Congratulations, you win: $1350

How much would you like to bet (1-100) or 0 to quit? 28

whirrrrr...and your pull is...
BAR       7       space
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 29

whirrrrr...and your pull is...
BAR       BAR       BAR
Congratulations, you win: $1450

How much would you like to bet (1-100) or 0 to quit? 30

whirrrrr...and your pull is...
BAR       7       BAR
Sorry, you lose.

How much would you like to bet (1-100) or 0 to quit? 0
Thanks for playing at the Casino!
Your individual winnings were:
0 0 0 20 250 0 35 0 135 0 0 0 0 0 0 0 0 0 95 100 0 0 0 360 1250 0 1350 0 1450 0
Your total winnings were: $5045

*******************************************************************************/

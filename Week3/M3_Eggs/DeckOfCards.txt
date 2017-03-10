import java.util.Scanner;
import java.util.Random;

/**
 * Programming Assignment 3: Deck of Cards
 * School: CSU, Monterey Bay
 * Course: CST 338 Software Design
 * Professor: Jesse Cecil, MS
 *
 * Java program with a virtual deck of cards that simulates a card game in a
 * player-to-player or player-to-computer scenario(s).
 *
 * @author Mariya Eggensperger
 * @author Kenneth Vader
 * @author Brittany Mazza
 */

public class DeckOfCards
{
   /**
    * Debug the deck of card classes.
    *
    * @param args command line arguments, which are not expected
    */
   public static void main(String[] args)
   {
      //
      // Test the Card Class
      //
      // Instantiate and display one illegal and two legal cards.
      Card card1 = new Card();
      Card card2 = new Card('K', Card.Suit.DIAMONDS);
      Card card3 = new Card('X', Card.Suit.HEARTS);
      System.out.println(card1.toString() + "\n"
                         + card2.toString() + "\n"
                         + card3.toString() + "\n");
      // Make a good card bad with an illegal value and change the initial
      // illegal card to a legal one.
      card1.set('z', Card.Suit.CLUBS);
      card3.set('T', Card.Suit.HEARTS);
      // Display current cards.
      System.out.println(card1.toString() + "\n"
                         + card2.toString() + "\n"
                         + card3.toString());
      //
      // Test the Hand class
      //
      // Create 2-5 explicit Card objects and one Hand object.
      Hand hand = new Hand();
      // Initialize car suit and value
      Card cardOne = new Card('K', Card.Suit.DIAMONDS);
      Card cardTwo = new Card('J', Card.Suit.HEARTS);
      Card cardThree = new Card('7', Card.Suit.SPADES);
      Card cardFour = new Card('2', Card.Suit.CLUBS);
      // Create an array of cards to iterate over them.
      Card[] handCards = {cardOne, cardTwo, cardThree, cardFour};
      while (hand.getNumCards() < Hand.MAX_CARDS)
      {
         // Add a card to the hand that's based on the remainder of the total
         // number of cards already in the hand over the number of cards that
         // should be added to the hand (giving us an even distribution of
         // cards in the hand).
         Card handCard = handCards[hand.getNumCards() % handCards.length];
         hand.takeCard(handCard);
      }
      // Display the hand.
      System.out.println("\nHand full\nAfter deal\n" + hand.toString());
      // Test inspectCard() with both legal and illegal int arguments.
      Card legalInspect = hand.inspectCard(Hand.MAX_CARDS - 1);
      Card illegalInspect = hand.inspectCard(Hand.MAX_CARDS);
      System.out.println("\nTesting inspectCard()\n" + legalInspect.toString()
                         + "\n" + illegalInspect.toString());
      // Next, play each card in a loop until the hand is empty. Display the
      // card played as it is played
      while (hand.getNumCards() > 0)
      {
         System.out.println("Playing " + hand.playCard().toString());
      }
      // Display the (now empty) hand, verifying hat no cards remain.
      System.out.println("\nAfter playing all cards\n" + hand.toString());
      //
      // Test Deck class
      //
      // Declare a deck containing two packs of cards.
      Deck deck = new Deck(2);
      // Deal all the cards in a loop until the deck is empty. Display each
      // card as it comes off the deck.
      while (deck.getTopCard() >= 0)
      {
         System.out.print(deck.dealCard() + " / ");
      }
      // Reset the deck by initializing it again (to the same two packs).
      deck.init(2);
      // Shuffle the deck and re-deal to the screen in a loop again. Notice
      // that the cards are now coming off in a random order.
      deck.shuffle();
      System.out.println("\n");
      while (deck.getTopCard() >= 0)
      {
         System.out.print(deck.dealCard() + " / ");
      }
      // Repeat this double deal, unshuffled, then shuffled, but this time using
      // a single pack deck.
      Deck singleDeck = new Deck();
      singleDeck.init(1);
      System.out.println("\n");
      while (singleDeck.getTopCard() >= 0)
      {
         System.out.print(singleDeck.dealCard() + " / ");
      }
      singleDeck.init(1);
      singleDeck.shuffle();
      System.out.println("\n");
      while (singleDeck.getTopCard() >= 0)
      {
         System.out.print(singleDeck.dealCard() + " / ");
      }
      //
      // Run a Simple Game
      //
      Scanner keyboard = new Scanner(System.in);
      int players;
      do
      {
         // Prompt the user to select the number of players (from 1-10).
         System.out.print("\n\nHow many hands? (1-10, please): ");
         players = keyboard.nextInt();
         // Gather the information again if the player's response is not valid.
         if (players < 0 || players > 10)
         {
            System.out.println("Hands must be between 1 and 10.");
         }
      } while (players < 0 || players > 10);
      // Instantiate a single-pack Deck object without shuffling, deal a deck
      // into that many Hand objects, dealing all cards until the deck is
      // empty.
      Deck singleGameDeck = new Deck();
      // Create player hands.
      Hand[] playerHands = new Hand[players];
      for (int i = 0; i < players; i++)
      {
         playerHands[i] = new Hand();
      }
      // Deal all of the cards to the players.
      while (singleGameDeck.getTopCard() >= 0)
      {
         for (Hand handElement : playerHands)
         {
            if (singleGameDeck.getTopCard() >= 0)
            {
               handElement.takeCard(singleGameDeck.dealCard());
            }
         }
      }
      // Display all the hands after the deal.
      System.out.println("Here are our hands, from unshuffled deck:");
      for (int i = 0; i < players; i++)
      {
         System.out.println(playerHands[i].toString());
      }
      // Reset the objects to their initial state, but this time shuffle the
      // deck before a second deal (same # of players).
      for (int i = 0; i < players; i++)
      {
         playerHands[i].resetHand();
      }
      singleGameDeck.init(1);
      singleGameDeck.shuffle();
      // Deal all of the cards to the players.
      while (singleGameDeck.getTopCard() >= 0)
      {
         for (Hand handElement : playerHands)
         {
            if (singleGameDeck.getTopCard() >= 0)
            {
               handElement.takeCard(singleGameDeck.dealCard());
            }
         }
      }
      System.out.println("\n\nHere are our hands, from SHUFFLED deck:");
      for (Hand playerHand : playerHands)
      {
         System.out.println(playerHand.toString());
      }
      keyboard.close();
   }
}

/**
 * Represents a single card of a larger card deck.
 * 1. Stores a char value to represent a card value, an enum value to represent
 *    suit, and a boolean value to represent an error flag.
 * 2. Provides 2 constructors:
 *    a. One a default with no arguments
 *    b. One requires 2 arguments, a value and a suit.
 * 3. Provides accessor methods to get values as needed.
 *    a. A set() method to set value, errorFlag, and suit. Set() will validate
 *       the card value passed to ensure only valid values are displayed.
 *    b. Getter methods for card value, card suit, and the error flag.
 */
class Card
{
   public enum Suit {CLUBS, DIAMONDS, HEARTS, SPADES}
   private char value;
   private boolean errorFlag;
   private Suit suit;
   
   /**
    * Overloaded constructor, allows for passing in Card value and Card suit.
    * Uses set method to set variables.
    *
    * @param value a valid value for Card char value
    * @param suit  a valid suit from Card enum suit
    */
   Card(char value, Suit suit)
   {
      set(value, suit);
   }
   
   /**
    * Default constructor for Card class that requires no arguments, sets value
    * to 'A' and suit to spades using set method.
    */
   Card()
   {
      // Because no parameters were passed, create card with the default
      // value of 'A' and spades.
      set('A', Suit.SPADES);
   }
   
   /**
    * Accessor method for Card suit.
    *
    * @return current value of suit
    */
   public Suit getSuit()
   {
      return suit;
   }
   
   /**
    * Accessor method for Card value.
    *
    * @return current contents of value
    */
   public char getValue()
   {
      return value;
   }
   
   /**
    * Accessor method for Card errorFlag.
    *
    * @return returns current value of errorFlag
    */
   public boolean getErrorFlag()
   {
      return errorFlag;
   }
   
   /**
    * Gets the card value display. If there is an error flag raised on the card,
    * the card display will indicate so an not return the card display.
    *
    * @return the card display
    */
   public String toString()
   {
      // If the error flag is true, return error message. Otherwise, return
      // string of Card value and Card suit.
      return errorFlag ? "** illegal **" : value + " of " + suit;
   }
   
   /**
    * Set Card value and Card suit. Sets errorFlag based on success or failure.
    * Uses isValid() to check validity of passed in value.
    *
    * @param value a Card char value
    * @param suit  a Card enum Suit (CLUBS, DIAMONDS, HEARTS, or SPADES)
    *
    * @return true if set was successful, false otherwise
    */
   public boolean set(char value, Suit suit)
   {
      if (isValid(value, suit))
      {
         this.value = value;
         this.suit = suit;
         errorFlag = false;
      }
      else
      {
         errorFlag = true;
      }
      return !errorFlag;
   }
   
   /**
    * Compare this card to a specific card to see if they're the same.
    *
    * @param card the card to compare to this card
    *
    * @return true if all the fields (members) are identical and false,
    *         otherwise
    */
   public boolean equals(Card card)
   {
      return suit == card.getSuit() && value == card.getValue()
      && errorFlag == card.getErrorFlag();
   }
   
   /**
    * Checks if the card value is valid.
    *
    * @param value a valid Card char value (must of value 1-9, T, J, Q, K, or
    *              A)
    * @param suit  a valid Suit from Card enum Suit
    *
    * @return true if the value is valid, false otherwise
    */
   private boolean isValid(char value, Suit suit)
   {
      // Suit does not need to be verified yet, but it is there for future use.
      switch (value)
      {
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case 'T':
         case 'J':
         case 'Q':
         case 'K':
         case 'A':
            return true;
         default:
            return false;
      }
   }
}

/**
 * An object of the type Hand represents a hand of cards. The hand is empty
 * when created and modified during the simulated game through increase/decrease
 * functions.
 */
class Hand
{
   public static final int MAX_CARDS = 52;
   
   private Card[] myCards;
   private int numCards;
   
   /**
    * Create a hand, containing zero elements.
    */
   public Hand()
   {
      myCards = new Card[MAX_CARDS];
   }
   
   /**
    * Remove all cards from the hand.
    */
   public void resetHand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }
   
   /**
    * Get the number of cards in this hand.
    *
    * @return the number of cards
    */
   public int getNumCards()
   {
      return numCards;
   }
   
   /**
    * Take a card from the deck and place it in this hand.
    *
    * @return true if successful, false otherwise
    */
   public boolean takeCard(Card card)
   {
      // Create an object copy (not a reference copy) of the card and add it
      // to the end of the hand.
      Card receivedCard = new Card();
      if (receivedCard.set(card.getValue(), card.getSuit()))
      {
         myCards[numCards] = receivedCard;
         numCards++;
         return true;
      }
      return false;
   }
   
   /**
    * Play card by removing it from this hand and returning it.
    *
    * @return the card to play
    */
   public Card playCard()
   {
      // Get and remove the card in the top occupied position of the hand (index
      // position of numCards - 1).
      Card playedCard = myCards[numCards - 1];
      myCards[numCards - 1] = null;
      numCards--;
      return playedCard;
   }
   
   /**
    * View an individual card in this hand.
    *
    * @param k the position of the card to view
    *
    * @return the card requested
    */
   public Card inspectCard(int k)
   {
      if (k >= 0 && k < myCards.length && myCards[k] != null)
      {
         return myCards[k];
      }
      // Returns a card with the error flag set to "true" if k is bad.
      Card errorCard = new Card();
      errorCard.set(' ', null); // Intentionally set errorFlag on card.
      return errorCard;
   }
   
   /**
    * Gets string display for the entire hand.
    *
    * @return the cards in this hand for display
    */
   public String toString()
   {
      String hand = "";
      for (int i = 0; i < numCards; i++)
      {
         hand += myCards[i].toString() + ", ";
      }
      return "Hand = ( " + hand + ")";
   }
}

/**
 * The Deck object is the source of all cards. It's where the dealer gets cards
 * to deal. If a player takes an individual card after the deal, they take it
 * from the Deck object.
 */
class Deck
{
   // Maximum cards number is six card packs or 312 cards (6 * 52 cards).
   public static final int MAX_CARDS = 312;
   private static Card[] masterPack;
   
   private Card[] cards;
   private int topCard;
   private int numPacks;
   
   /**
    * Create a new card deck.
    *
    * @param numPacks the number of pack to initialize the new card deck with
    */
   public Deck(int numPacks)
   {
      // Create master pack the cards.
      allocateMasterPack();
      init(numPacks);
   }
   
   /**
    * Create a new card deck. Deck will be initialized with one pack.
    */
   public Deck()
   {
      // Create master pack the cards.
      allocateMasterPack();
      init(1);
   }
   
   /**
    * Get the location of the top card.
    *
    * @return the location of the top card
    */
   public int getTopCard()
   {
      return topCard;
   }
   
   /**
    * Initialize the card deck to a new, initial state.
    *
    * @param numPacks the number of packs to initial the deck with
    */
   public void init(int numPacks)
   {
      this.numPacks = numPacks;
      topCard = -1;
      // Re-populate cards[] with the standard 52 numPacks cards.
      cards = new Card[numPacks * 52];
      
      for (int i = 0; i < numPacks; i++)
      {
         for (Card card : masterPack)
         {
            topCard++;
            cards[topCard] = card;
         }
      }
   }
   
   /**
    * Shuffle the deck.
    */
   public void shuffle()
   {
      int newIndex;
      Card temp;
      Random randomIndex = new Random();
      
      // We start at the bottom of the card[] array (position 51 in a 52 card
      // deck). Each time through the loop we pick a random location that is
      // higher up in the deck and perform a swap.
      for (int i = cards.length - 1; i > 0; i--)
      {
         newIndex = randomIndex.nextInt(i);
         temp = cards[i];
         cards[i] = cards[newIndex];
         cards[newIndex] = temp;
      }
   }
   
   /**
    * Pull a card from the top of the deck.
    *
    * @return the top card in the deck
    */
   public Card dealCard()
   {
      // Remove and return the top card in the deck.
      Card dealtCard = cards[topCard];
      cards[topCard] = null;
      topCard--;
      return dealtCard;
   }
   
   /**
    * View the card at a particular location.
    *
    * @param k the location of the card to view
    *
    * @return the card at the specified location
    */
   public Card inspectCard(int k)
   {
      if (k >= 0 && k < cards.length && cards[k] != null)
      {
         return cards[k];
      }
      // Returns a card with the error flag set to "true" if k is bad.
      Card errorCard = new Card();
      errorCard.set(' ', null); // Intentionally set errorFlag.
      return errorCard;
   }
   
   /**
    * Create a single, static master card deck. This deck will not contain
    * card duplicates and will be the sole reference for the card types. If
    * the master card deck hass already been allocated, this method will not
    * run again.
    */
   private static void allocateMasterPack()
   {
      if (masterPack != null)
      {
         return; // Return early if pack is already allocated.
      }
      // Create master pack where there is one of each card type.
      Card.Suit[] cardSuits = new Card.Suit[]{Card.Suit.SPADES, Card.Suit.CLUBS,
         Card.Suit.DIAMONDS, Card.Suit.HEARTS};
      char[] cardValues = new char[] {'2', '3', '4', '5', '6', '7', '8',
         '9', 'T', 'J', 'Q', 'K', 'A'};
      int insertPosition = 0;
      // The card suits multiplied by the card values should give us 52, a
      // standard deck.
      masterPack = new Card[cardSuits.length * cardValues.length];
      for (Card.Suit cardSuit : cardSuits)
      {
         for (char cardValue : cardValues)
         {
            masterPack[insertPosition] = new Card(cardValue, cardSuit);
            insertPosition++;
         }
      }
   }
}

/*********************************** OUTPUT TEST 1 *****************************
 
 A of SPADES
 K of DIAMONDS
 ** illegal **
 
 ** illegal **
 K of DIAMONDS
 T of HEARTS
 
 Hand full
 After deal
 Hand = ( K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, )
 
 Testing inspectCard()
 2 of CLUBS
 ** illegal **
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 
 After playing all cards
 Hand = ( )
 A of HEARTS / K of HEARTS / Q of HEARTS / J of HEARTS / T of HEARTS / 9 of HEARTS / 8 of HEARTS / 7 of HEARTS / 6 of HEARTS / 5 of HEARTS / 4 of HEARTS / 3 of HEARTS / 2 of HEARTS / A of DIAMONDS / K of DIAMONDS / Q of DIAMONDS / J of DIAMONDS / T of DIAMONDS / 9 of DIAMONDS / 8 of DIAMONDS / 7 of DIAMONDS / 6 of DIAMONDS / 5 of DIAMONDS / 4 of DIAMONDS / 3 of DIAMONDS / 2 of DIAMONDS / A of CLUBS / K of CLUBS / Q of CLUBS / J of CLUBS / T of CLUBS / 9 of CLUBS / 8 of CLUBS / 7 of CLUBS / 6 of CLUBS / 5 of CLUBS / 4 of CLUBS / 3 of CLUBS / 2 of CLUBS / A of SPADES / K of SPADES / Q of SPADES / J of SPADES / T of SPADES / 9 of SPADES / 8 of SPADES / 7 of SPADES / 6 of SPADES / 5 of SPADES / 4 of SPADES / 3 of SPADES / 2 of SPADES / A of HEARTS / K of HEARTS / Q of HEARTS / J of HEARTS / T of HEARTS / 9 of HEARTS / 8 of HEARTS / 7 of HEARTS / 6 of HEARTS / 5 of HEARTS / 4 of HEARTS / 3 of HEARTS / 2 of HEARTS / A of DIAMONDS / K of DIAMONDS / Q of DIAMONDS / J of DIAMONDS / T of DIAMONDS / 9 of DIAMONDS / 8 of DIAMONDS / 7 of DIAMONDS / 6 of DIAMONDS / 5 of DIAMONDS / 4 of DIAMONDS / 3 of DIAMONDS / 2 of DIAMONDS / A of CLUBS / K of CLUBS / Q of CLUBS / J of CLUBS / T of CLUBS / 9 of CLUBS / 8 of CLUBS / 7 of CLUBS / 6 of CLUBS / 5 of CLUBS / 4 of CLUBS / 3 of CLUBS / 2 of CLUBS / A of SPADES / K of SPADES / Q of SPADES / J of SPADES / T of SPADES / 9 of SPADES / 8 of SPADES / 7 of SPADES / 6 of SPADES / 5 of SPADES / 4 of SPADES / 3 of SPADES / 2 of SPADES /
 
 9 of SPADES / J of CLUBS / A of CLUBS / J of DIAMONDS / 5 of DIAMONDS / J of CLUBS / 3 of CLUBS / T of HEARTS / J of SPADES / 8 of DIAMONDS / T of SPADES / 3 of DIAMONDS / 3 of DIAMONDS / Q of CLUBS / A of HEARTS / 5 of HEARTS / 3 of SPADES / 5 of HEARTS / K of DIAMONDS / 8 of HEARTS / 5 of DIAMONDS / 6 of HEARTS / 6 of HEARTS / 4 of CLUBS / A of SPADES / T of CLUBS / 7 of CLUBS / 8 of SPADES / 2 of SPADES / A of DIAMONDS / 6 of CLUBS / 2 of CLUBS / 8 of DIAMONDS / Q of HEARTS / 9 of CLUBS / J of HEARTS / T of DIAMONDS / 5 of SPADES / 9 of SPADES / 2 of DIAMONDS / 9 of HEARTS / 3 of HEARTS / 8 of CLUBS / 7 of DIAMONDS / 2 of SPADES / 9 of CLUBS / 4 of DIAMONDS / A of HEARTS / 5 of CLUBS / 3 of HEARTS / K of SPADES / 8 of SPADES / A of SPADES / 5 of SPADES / 4 of HEARTS / Q of DIAMONDS / 9 of DIAMONDS / 2 of CLUBS / J of SPADES / J of HEARTS / 2 of DIAMONDS / K of CLUBS / Q of SPADES / 4 of HEARTS / 8 of CLUBS / Q of CLUBS / 7 of DIAMONDS / 6 of SPADES / T of DIAMONDS / T of HEARTS / 4 of CLUBS / T of CLUBS / 7 of SPADES / 7 of HEARTS / 4 of SPADES / T of SPADES / Q of HEARTS / 3 of SPADES / 4 of DIAMONDS / 9 of DIAMONDS / 7 of SPADES / K of HEARTS / 7 of HEARTS / 8 of HEARTS / 6 of CLUBS / 4 of SPADES / K of HEARTS / 6 of DIAMONDS / K of DIAMONDS / A of CLUBS / Q of DIAMONDS / J of DIAMONDS / 2 of HEARTS / K of CLUBS / 9 of HEARTS / A of DIAMONDS / 2 of HEARTS / 3 of CLUBS / K of SPADES / 6 of DIAMONDS / 7 of CLUBS / 5 of CLUBS / 6 of SPADES / Q of SPADES /
 
 A of HEARTS / K of HEARTS / Q of HEARTS / J of HEARTS / T of HEARTS / 9 of HEARTS / 8 of HEARTS / 7 of HEARTS / 6 of HEARTS / 5 of HEARTS / 4 of HEARTS / 3 of HEARTS / 2 of HEARTS / A of DIAMONDS / K of DIAMONDS / Q of DIAMONDS / J of DIAMONDS / T of DIAMONDS / 9 of DIAMONDS / 8 of DIAMONDS / 7 of DIAMONDS / 6 of DIAMONDS / 5 of DIAMONDS / 4 of DIAMONDS / 3 of DIAMONDS / 2 of DIAMONDS / A of CLUBS / K of CLUBS / Q of CLUBS / J of CLUBS / T of CLUBS / 9 of CLUBS / 8 of CLUBS / 7 of CLUBS / 6 of CLUBS / 5 of CLUBS / 4 of CLUBS / 3 of CLUBS / 2 of CLUBS / A of SPADES / K of SPADES / Q of SPADES / J of SPADES / T of SPADES / 9 of SPADES / 8 of SPADES / 7 of SPADES / 6 of SPADES / 5 of SPADES / 4 of SPADES / 3 of SPADES / 2 of SPADES /
 
 Q of SPADES / Q of HEARTS / 3 of CLUBS / K of SPADES / 7 of HEARTS / T of HEARTS / 6 of SPADES / 8 of CLUBS / 4 of SPADES / 2 of HEARTS / 6 of CLUBS / J of HEARTS / 7 of SPADES / 4 of DIAMONDS / J of SPADES / 3 of DIAMONDS / 4 of HEARTS / 2 of CLUBS / J of DIAMONDS / 9 of CLUBS / Q of CLUBS / 6 of HEARTS / 9 of DIAMONDS / 5 of SPADES / 9 of HEARTS / A of HEARTS / 3 of HEARTS / Q of DIAMONDS / 5 of HEARTS / K of CLUBS / A of CLUBS / T of DIAMONDS / 8 of DIAMONDS / 7 of DIAMONDS / 8 of SPADES / 7 of CLUBS / T of SPADES / 5 of DIAMONDS / 3 of SPADES / T of CLUBS / 6 of DIAMONDS / 2 of SPADES / K of HEARTS / 8 of HEARTS / J of CLUBS / 4 of CLUBS / A of DIAMONDS / A of SPADES / K of DIAMONDS / 2 of DIAMONDS / 5 of CLUBS / 9 of SPADES /
 
 How many hands? (1-10, please): 102
 Hands must be between 1 and 10.
 
 
 How many hands? (1-10, please): 6
 Here are our hands, from unshuffled deck:
 Hand = ( A of HEARTS, 8 of HEARTS, 2 of HEARTS, 9 of DIAMONDS, 3 of DIAMONDS, T of CLUBS, 4 of CLUBS, J of SPADES, 5 of SPADES, )
 Hand = ( K of HEARTS, 7 of HEARTS, A of DIAMONDS, 8 of DIAMONDS, 2 of DIAMONDS, 9 of CLUBS, 3 of CLUBS, T of SPADES, 4 of SPADES, )
 Hand = ( Q of HEARTS, 6 of HEARTS, K of DIAMONDS, 7 of DIAMONDS, A of CLUBS, 8 of CLUBS, 2 of CLUBS, 9 of SPADES, 3 of SPADES, )
 Hand = ( J of HEARTS, 5 of HEARTS, Q of DIAMONDS, 6 of DIAMONDS, K of CLUBS, 7 of CLUBS, A of SPADES, 8 of SPADES, 2 of SPADES, )
 Hand = ( T of HEARTS, 4 of HEARTS, J of DIAMONDS, 5 of DIAMONDS, Q of CLUBS, 6 of CLUBS, K of SPADES, 7 of SPADES, )
 Hand = ( 9 of HEARTS, 3 of HEARTS, T of DIAMONDS, 4 of DIAMONDS, J of CLUBS, 5 of CLUBS, Q of SPADES, 6 of SPADES, )
 
 
 Here are our hands, from SHUFFLED deck:
 Hand = ( 9 of DIAMONDS, 7 of SPADES, T of SPADES, 5 of CLUBS, 3 of SPADES, J of CLUBS, A of HEARTS, Q of HEARTS, Q of CLUBS, )
 Hand = ( 7 of DIAMONDS, 3 of CLUBS, 5 of DIAMONDS, 9 of HEARTS, K of SPADES, 2 of HEARTS, 4 of SPADES, 3 of HEARTS, Q of DIAMONDS, )
 Hand = ( J of DIAMONDS, 2 of SPADES, Q of SPADES, 5 of HEARTS, T of DIAMONDS, 7 of HEARTS, K of HEARTS, 2 of CLUBS, K of CLUBS, )
 Hand = ( T of CLUBS, 6 of DIAMONDS, 4 of DIAMONDS, J of SPADES, 6 of CLUBS, A of DIAMONDS, 4 of CLUBS, 2 of DIAMONDS, 7 of CLUBS, )
 Hand = ( A of CLUBS, 5 of SPADES, J of HEARTS, K of DIAMONDS, 8 of CLUBS, 8 of DIAMONDS, 6 of HEARTS, 8 of SPADES, )
 Hand = ( 4 of HEARTS, 9 of SPADES, A of SPADES, 6 of SPADES, 8 of HEARTS, 9 of CLUBS, 3 of DIAMONDS, T of HEARTS, )
 
 ******************************************************************************/
/*********************************** OUTPUT TEST 2 *****************************
 
 A of SPADES
 K of DIAMONDS
 ** illegal **
 
 ** illegal **
 K of DIAMONDS
 T of HEARTS
 
 Hand full
 After deal
 Hand = ( K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, K of DIAMONDS, J of HEARTS, 7 of SPADES, 2 of CLUBS, )
 
 Testing inspectCard()
 2 of CLUBS
 ** illegal **
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 Playing 2 of CLUBS
 Playing 7 of SPADES
 Playing J of HEARTS
 Playing K of DIAMONDS
 
 After playing all cards
 Hand = ( )
 A of HEARTS / K of HEARTS / Q of HEARTS / J of HEARTS / T of HEARTS / 9 of HEARTS / 8 of HEARTS / 7 of HEARTS / 6 of HEARTS / 5 of HEARTS / 4 of HEARTS / 3 of HEARTS / 2 of HEARTS / A of DIAMONDS / K of DIAMONDS / Q of DIAMONDS / J of DIAMONDS / T of DIAMONDS / 9 of DIAMONDS / 8 of DIAMONDS / 7 of DIAMONDS / 6 of DIAMONDS / 5 of DIAMONDS / 4 of DIAMONDS / 3 of DIAMONDS / 2 of DIAMONDS / A of CLUBS / K of CLUBS / Q of CLUBS / J of CLUBS / T of CLUBS / 9 of CLUBS / 8 of CLUBS / 7 of CLUBS / 6 of CLUBS / 5 of CLUBS / 4 of CLUBS / 3 of CLUBS / 2 of CLUBS / A of SPADES / K of SPADES / Q of SPADES / J of SPADES / T of SPADES / 9 of SPADES / 8 of SPADES / 7 of SPADES / 6 of SPADES / 5 of SPADES / 4 of SPADES / 3 of SPADES / 2 of SPADES / A of HEARTS / K of HEARTS / Q of HEARTS / J of HEARTS / T of HEARTS / 9 of HEARTS / 8 of HEARTS / 7 of HEARTS / 6 of HEARTS / 5 of HEARTS / 4 of HEARTS / 3 of HEARTS / 2 of HEARTS / A of DIAMONDS / K of DIAMONDS / Q of DIAMONDS / J of DIAMONDS / T of DIAMONDS / 9 of DIAMONDS / 8 of DIAMONDS / 7 of DIAMONDS / 6 of DIAMONDS / 5 of DIAMONDS / 4 of DIAMONDS / 3 of DIAMONDS / 2 of DIAMONDS / A of CLUBS / K of CLUBS / Q of CLUBS / J of CLUBS / T of CLUBS / 9 of CLUBS / 8 of CLUBS / 7 of CLUBS / 6 of CLUBS / 5 of CLUBS / 4 of CLUBS / 3 of CLUBS / 2 of CLUBS / A of SPADES / K of SPADES / Q of SPADES / J of SPADES / T of SPADES / 9 of SPADES / 8 of SPADES / 7 of SPADES / 6 of SPADES / 5 of SPADES / 4 of SPADES / 3 of SPADES / 2 of SPADES /
 
 7 of CLUBS / Q of DIAMONDS / 6 of SPADES / 6 of DIAMONDS / 7 of DIAMONDS / 9 of SPADES / 3 of SPADES / A of DIAMONDS / 7 of CLUBS / K of CLUBS / J of CLUBS / K of HEARTS / J of SPADES / A of SPADES / J of HEARTS / J of CLUBS / 7 of SPADES / Q of HEARTS / 9 of CLUBS / J of DIAMONDS / 8 of DIAMONDS / 5 of CLUBS / 6 of HEARTS / T of HEARTS / 4 of CLUBS / 4 of DIAMONDS / 5 of DIAMONDS / A of HEARTS / 3 of DIAMONDS / 2 of SPADES / 4 of HEARTS / 6 of CLUBS / A of CLUBS / 2 of HEARTS / J of SPADES / 8 of CLUBS / A of DIAMONDS / 9 of HEARTS / 5 of HEARTS / 7 of SPADES / 2 of DIAMONDS / 9 of DIAMONDS / 3 of HEARTS / 5 of DIAMONDS / 8 of HEARTS / 7 of DIAMONDS / 5 of CLUBS / 4 of CLUBS / 8 of DIAMONDS / 4 of SPADES / T of HEARTS / 6 of DIAMONDS / 2 of CLUBS / 3 of CLUBS / 9 of HEARTS / 8 of CLUBS / J of DIAMONDS / Q of SPADES / A of SPADES / 8 of SPADES / 3 of HEARTS / 6 of SPADES / T of CLUBS / 3 of DIAMONDS / 9 of SPADES / Q of SPADES / 4 of DIAMONDS / K of DIAMONDS / 2 of CLUBS / 9 of DIAMONDS / 2 of DIAMONDS / 6 of CLUBS / 5 of SPADES / K of DIAMONDS / T of SPADES / T of CLUBS / T of SPADES / A of CLUBS / Q of CLUBS / J of HEARTS / 4 of HEARTS / K of SPADES / 3 of SPADES / 7 of HEARTS / K of SPADES / 8 of SPADES / Q of CLUBS / 6 of HEARTS / 9 of CLUBS / T of DIAMONDS / 2 of HEARTS / 2 of SPADES / Q of HEARTS / 8 of HEARTS / 7 of HEARTS / A of HEARTS / T of DIAMONDS / Q of DIAMONDS / K of CLUBS / 5 of HEARTS / 4 of SPADES / 5 of SPADES / 3 of CLUBS / K of HEARTS /
 
 A of HEARTS / K of HEARTS / Q of HEARTS / J of HEARTS / T of HEARTS / 9 of HEARTS / 8 of HEARTS / 7 of HEARTS / 6 of HEARTS / 5 of HEARTS / 4 of HEARTS / 3 of HEARTS / 2 of HEARTS / A of DIAMONDS / K of DIAMONDS / Q of DIAMONDS / J of DIAMONDS / T of DIAMONDS / 9 of DIAMONDS / 8 of DIAMONDS / 7 of DIAMONDS / 6 of DIAMONDS / 5 of DIAMONDS / 4 of DIAMONDS / 3 of DIAMONDS / 2 of DIAMONDS / A of CLUBS / K of CLUBS / Q of CLUBS / J of CLUBS / T of CLUBS / 9 of CLUBS / 8 of CLUBS / 7 of CLUBS / 6 of CLUBS / 5 of CLUBS / 4 of CLUBS / 3 of CLUBS / 2 of CLUBS / A of SPADES / K of SPADES / Q of SPADES / J of SPADES / T of SPADES / 9 of SPADES / 8 of SPADES / 7 of SPADES / 6 of SPADES / 5 of SPADES / 4 of SPADES / 3 of SPADES / 2 of SPADES /
 
 T of HEARTS / A of CLUBS / 8 of HEARTS / 2 of DIAMONDS / K of CLUBS / 5 of SPADES / Q of CLUBS / T of SPADES / 7 of HEARTS / 5 of DIAMONDS / 8 of CLUBS / 6 of HEARTS / K of DIAMONDS / 6 of SPADES / 4 of DIAMONDS / 8 of SPADES / K of SPADES / A of SPADES / 2 of CLUBS / 3 of SPADES / 4 of SPADES / 4 of CLUBS / J of HEARTS / Q of DIAMONDS / 6 of DIAMONDS / 9 of DIAMONDS / 4 of HEARTS / 2 of HEARTS / K of HEARTS / 5 of HEARTS / J of DIAMONDS / 9 of HEARTS / 2 of SPADES / 9 of SPADES / T of DIAMONDS / 7 of CLUBS / 5 of CLUBS / J of SPADES / 9 of CLUBS / A of DIAMONDS / 3 of HEARTS / 3 of CLUBS / A of HEARTS / 7 of SPADES / 6 of CLUBS / 3 of DIAMONDS / Q of SPADES / 8 of DIAMONDS / 7 of DIAMONDS / T of CLUBS / Q of HEARTS / J of CLUBS /
 
 How many hands? (1-10, please): 1
 Here are our hands, from unshuffled deck:
 Hand = ( A of HEARTS, K of HEARTS, Q of HEARTS, J of HEARTS, T of HEARTS, 9 of HEARTS, 8 of HEARTS, 7 of HEARTS, 6 of HEARTS, 5 of HEARTS, 4 of HEARTS, 3 of HEARTS, 2 of HEARTS, A of DIAMONDS, K of DIAMONDS, Q of DIAMONDS, J of DIAMONDS, T of DIAMONDS, 9 of DIAMONDS, 8 of DIAMONDS, 7 of DIAMONDS, 6 of DIAMONDS, 5 of DIAMONDS, 4 of DIAMONDS, 3 of DIAMONDS, 2 of DIAMONDS, A of CLUBS, K of CLUBS, Q of CLUBS, J of CLUBS, T of CLUBS, 9 of CLUBS, 8 of CLUBS, 7 of CLUBS, 6 of CLUBS, 5 of CLUBS, 4 of CLUBS, 3 of CLUBS, 2 of CLUBS, A of SPADES, K of SPADES, Q of SPADES, J of SPADES, T of SPADES, 9 of SPADES, 8 of SPADES, 7 of SPADES, 6 of SPADES, 5 of SPADES, 4 of SPADES, 3 of SPADES, 2 of SPADES, )
 
 
 Here are our hands, from SHUFFLED deck:
 Hand = ( 2 of HEARTS, 3 of DIAMONDS, 8 of DIAMONDS, K of DIAMONDS, A of DIAMONDS, Q of DIAMONDS, T of SPADES, 3 of CLUBS, T of HEARTS, T of DIAMONDS, J of DIAMONDS, 6 of CLUBS, 8 of HEARTS, 5 of DIAMONDS, 8 of CLUBS, A of SPADES, 6 of DIAMONDS, 4 of SPADES, 7 of HEARTS, 2 of SPADES, 9 of SPADES, T of CLUBS, Q of SPADES, Q of CLUBS, 4 of CLUBS, 8 of SPADES, A of HEARTS, K of SPADES, K of CLUBS, 7 of DIAMONDS, J of HEARTS, 6 of SPADES, Q of HEARTS, 5 of SPADES, J of SPADES, 2 of CLUBS, 4 of DIAMONDS, 5 of HEARTS, 9 of DIAMONDS, 9 of CLUBS, 6 of HEARTS, J of CLUBS, 3 of SPADES, 7 of CLUBS, A of CLUBS, 5 of CLUBS, 3 of HEARTS, 7 of SPADES, 9 of HEARTS, 4 of HEARTS, 2 of DIAMONDS, K of HEARTS, )
 
 ******************************************************************************/

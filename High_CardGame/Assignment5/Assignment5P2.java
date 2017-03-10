import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.util.*;

/**
 * Programming Assignment 5, Part 2: Encapsulating Layout and Icons into
 * CardTable and GUICard Classes
 * School: CSU, Monterey Bay
 * Course: CST 338 Software Design
 * Professor: Jesse Cecil, MS
 *
 * Control the positioning of the panels and cards of the GUI and manages the
 * reading and building of the card image Icons.
 *
 * @author Mariya Eggensperger
 * @author Kenneth Vader
 * @author Brittany Mazza
 */
public class Assignment5P2
{
   static int NUM_CARDS_PER_HAND = 7;
   static int NUM_PLAYERS = 2;

   // Create new storage for JLabels
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];

   /**
    * A simple main to throw all the JLabels.
    * Tests all three phases of the the program.
    * Note this method is the public class of each program.
    *
    * @param args command line argument - none expected
    */
   public static void main(String[] args)
   {
      // Establish main frame in which program will run
      CardTable myCardTable = new CardTable("CardTable", NUM_CARDS_PER_HAND,
            NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      // CREATE LABELS ---------------------------------------------------------
      // Create new GUICard to initialize the GUICard class. The GUICard
      // static methods called later will not work without doing this.
      new GUICard();
      int k; // Iteration integer for setting JLabels
      // Computer Hand
      for (k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         computerLabels[k] = new JLabel(GUICard.getBackCardIcon());
      }
      // Human Hand
      for (k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         // Returns a new random card for the main to use in its tests.
         humanLabels[k] = new JLabel(GUICard.getIcon(generateRandomCard()));
      }
      // Simulation of computer vs. human player
      for (k = 0; k < NUM_PLAYERS; k++)
      {
         Icon cardIcon = GUICard.getIcon(generateRandomCard());
         playedCardLabels[k] = new JLabel(cardIcon);
         playedCardLabels[k].setText(k == 0 ? "Computer" : "You");
         playedCardLabels[k].setHorizontalTextPosition(JLabel.CENTER);
         playedCardLabels[k].setVerticalTextPosition(JLabel.BOTTOM);
      }

      // ADD LABELS TO PANELS --------------------------------------------------
      // Play Area
      myCardTable.pnlPlayArea.add(playedCardLabels[0]);
      myCardTable.pnlPlayArea.add(playedCardLabels[1]);
      // Human Hand
      for (k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         myCardTable.pnlHumanHand.add(humanLabels[k]);
      }
      // Computer Hand
      for (k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         myCardTable.pnlComputerHand.add(computerLabels[k]);
      }
      // Show everything to the user.
      myCardTable.setVisible(true);
   }

   /**
    * Provide a random new card.
    *
    * @return random new card
    */
   private static Card generateRandomCard()
   {
      // Get random value & suit.
      Random random = new Random();
      int randomValuePosition = random.nextInt(Card.cardValues.length);
      int randomSuitPosition = random.nextInt(Card.Suit.values().length);
      char randomValue = Card.cardValues[randomValuePosition];
      Card.Suit randomSuit = Card.Suit.values()[randomSuitPosition];

      // Return random new card with values and suits.
      return new Card(randomValue, randomSuit);
   }
}

/**
 * The frame that displays the card game, which includes the computer's hand,
 * human hand, and the played cards.
 */
class CardTable extends JFrame
{
   static int MAX_CARDS_PER_HAND = 56; // Cards include jokers
   static int MAX_PLAYERS = 2;  // Only 2 players allowed in the game

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   /**
    * Create a new card table.
    *
    * @param title           the title for the card table
    * @param numCardsPerHand the number of cards a player can hold
    * @param numPlayers      the number of players
    */
   CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      super();
      setTitle(title);
      // Set the number of cards per hand and the number of players to 0 if
      // their parameters are not in a valid range.
      boolean isNumCardsPerHandInRange = numCardsPerHand > 0
            && numCardsPerHand <= MAX_CARDS_PER_HAND;
      boolean isNumPlayersInRange = numPlayers > 1 && numPlayers <= MAX_PLAYERS;
      this.numCardsPerHand = isNumCardsPerHandInRange ? numCardsPerHand : 0;
      this.numPlayers = isNumPlayersInRange ? numPlayers : 2;
      // Add panels to the JFrame. We want a row for each player and a
      // playing row. The order will be opponents first (computer in this
      // case), playing area, and then the player's hand.
      GridLayout tableLayout = new GridLayout(numPlayers + 1, 1);
      setLayout(tableLayout);
      // Create opponent's hand panels. This is done dynamically so there could
      // be many opponents hands displayed at the top of the window even
      // though we only have a 2 player maximum set right now.
      JPanel[] opponentPanels = new JPanel[numPlayers - 1];
      for (int i = 0; i < numPlayers - 1; i++)
      {
         Border opponentBorder = BorderFactory.createTitledBorder(
               "Opponent #" + (i + 1));
         GridLayout opponentLayout = new GridLayout(1, numCardsPerHand);
         opponentPanels[i] = new JPanel();
         opponentPanels[i].setBorder(opponentBorder);
         opponentPanels[i].setLayout(opponentLayout);
      }
      // Only add first opponent panel as member variable on this class.
      pnlComputerHand = opponentPanels[0];
      // Create player's hand panel.
      Border playerHandBorder = BorderFactory.createTitledBorder("Your Hand");
      GridLayout playerHandLayout = new GridLayout(1, numCardsPerHand);
      pnlHumanHand = new JPanel();
      pnlHumanHand.setBorder(playerHandBorder);
      pnlHumanHand.setLayout(playerHandLayout);
      // Create playing area panel.
      Border playingAreaBorder = BorderFactory.createTitledBorder(
            "Playing Area");
      GridLayout playAreaLayout = new GridLayout(1, 2);
      pnlPlayArea = new JPanel();
      pnlPlayArea.setBorder(playingAreaBorder);
      pnlPlayArea.setLayout(playAreaLayout);

      // Add panels to window.
      add(pnlComputerHand);
      add(pnlPlayArea);
      add(pnlHumanHand);
   }

   /**
    * Get the number of cards per hand.
    *
    * @return the number of cards per hand
    */
   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }

   /**
    * Get the number of players.
    *
    * @return the number of players
    */
   public int getNumPlayers()
   {
      return numPlayers;
   }
}

/**
 * Manage the reading and building of the card image Icons.
 */
class GUICard
{
   // 14 = A through K + joker and 4 = # of Suits for iconCards.
   private static String[] cardSuit = new String[]{"C", "D", "H", "S"};
   private static Icon[][] iconCards = new ImageIcon[14][4];
   private static Icon iconBack;
   static boolean iconsLoaded = false;

   /**
    * Create a new card GUI. This will load all of the card icons.
    */
   public GUICard()
   {
      // Call on the loadCardIcons() method to
      // instantiate each of 57 icons in icon[] array
      loadCardIcons();
   }

   /**
    * Build files names. Instantiate 57 icons in icon[] array.
    */
   static void loadCardIcons()
   {
      // Don't load icons again if they've already been loaded.
      if (iconsLoaded)
      {
         return;
      }
      // Create icons using the card file name and their location.
      String[][] fileNames = getFileNames();
      iconBack = new ImageIcon("images/BK.gif");
      for (int i = 0; i < 14; i++)
      {
         for (int j = 0; j < 4; j++)
         {
            iconCards[i][j] = new ImageIcon("images/" + fileNames[i][j]);
         }
      }
      iconsLoaded = true;
   }

   /**
    * Get all of the image file names for each card in a standard deck.
    *
    * @return the image file names all card types
    */
   private static String[][] getFileNames()
   {
      String[][] fileNames = new String[14][4];
      // Create the file names to be used later for creating icons.
      // The card back is a special case and will be added explicitly.
      for (int i = 0; i < 4; i++) // 4 card suits available
      {
         String cardSuit = turnIntIntoCardSuit(i);
         for (int j = 0; j < 14; j++) // 14 card suits available
         {
            String cardValue = turnIntIntoCardValue(j);
            fileNames[j][i] = cardValue + cardSuit + ".gif";
         }
      }
      return fileNames;
   }

   /**
    * Get a card value based on its integer representation, where 0 - 13
    * represent "A", "2", "3", ... "Q", "K", and "X".
    *
    * @param k the integer that represents a card value
    *
    * @return the card value represented by the requested integer
    */
   static String turnIntIntoCardValue(int k)
   {
      // Where int k is the returned card into card Value
      return String.valueOf(Card.cardValues[k]);
   }

   /**
    * Get a card suit based on its integer representation, where 0 - 3
    * represent "C", "D", "H", and "S".
    *
    * @param j the integer that represents a card suit
    *
    * @return the card suit represented by the requested integer
    */
   static String turnIntIntoCardSuit(int j)
   {
      // Where int j is the returned int into card Suit
      return cardSuit[j];
   }

   /**
    * Get an integer representation of a card value for a requested card.
    *
    * @param card the card to get the value position for
    *
    * @return the value position represented by an integer
    */
   private static int valueAsInt(Card card)
   {
      String values = new String(Card.cardValues);
      return values.indexOf(card.getValue());
   }

   /**
    * Get an integer representation of a card suit for a requested card.
    *
    * @param card the card to get the suit position for
    *
    * @return the suit position represented by an integer
    */
   private static int suitAsInt(Card card)
   {
      return card.getSuit().ordinal();
   }

   /**
    * Get the icon associated with the card requested.
    *
    * @param card the card to get an associated icon of
    *
    * @return the icon for the card requested
    */
   static public Icon getIcon(Card card)
   {
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

   /**
    * Get back of card image icon.
    *
    * @return back of card image icon
    */
   static public Icon getBackCardIcon()
   {
      return iconBack;
   }
}

/**
 * A playing card.
 */
class Card
{
   public enum Suit
   {
      CLUBS, DIAMONDS, HEARTS, SPADES
   }

   private char value;
   private boolean errorFlag;
   private Suit suit;

   // Set card values.
   public static char[] cardValues = {'A', '2', '3', '4', '5', '6', '7', '8',
         '9', 'T', 'J', 'Q', 'K', 'X'};

   // Order the card values with smallest first, include 'X' for a joker/
   public static char[] valueRanks = {'2', '3', '4', '5', '6', '7', '8', '9',
         'T', 'J', 'Q', 'K', 'A', 'X'};

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
      errorFlag = false;
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
    * Sort incoming array of cards using a bubble sort routine.
    *
    * @param cards     the cards to sort
    * @param arraySize the size of the array
    */
   static void arraySort(Card[] cards, int arraySize)
   {
      Card temp;
      for (int i = 0; i < arraySize; i++)
      {
         for (int j = 1; j < arraySize - i; j++)
         {
            if (cards[j] == null)
            {
               return;
            }
            int previousRankIndex = findValueRankIndex(cards[j - 1].getValue());
            int currentRankIndex = findValueRankIndex(cards[j].getValue());
            if (previousRankIndex > currentRankIndex)
            {
               temp = cards[j - 1];
               cards[j - 1] = cards[j];
               cards[j] = temp;
            }
         }
      }
   }

   /**
    * Get the rank for the card value, which is the index position of it
    * within the valueRanks array.
    *
    * @param cardValue a card value
    *
    * @return the value rank index
    */
   private static int findValueRankIndex(char cardValue)
   {
      for (int i = 0; i < valueRanks.length; i++)
      {
         if (cardValue == valueRanks[i])
         {
            return i;
         }
      }
      return 0;
   }

   /**
    * Compare this card to a specific card to see if they're the same.
    *
    * @param card the card to compare to this card
    *
    * @return true if all the fields (members) are identical and false,
    * otherwise
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
         case 'X': // for joker
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
      // Default constructor
      myCards = new Card[MAX_CARDS];
      // Set num cards to zero
      numCards = 0;
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

   /**
    * Sort the hand by calling on arraySort() method.
    */
   public void sort()
   {
      Card.arraySort(myCards, myCards.length);
   }
}

/**
 * The Deck object is the source of all cards. It's where the dealer gets cards
 * to deal. If a player takes an individual card after the deal, they take it
 * from the Deck object.
 */
class Deck
{
   // Maximum cards number is six card packs or 336 cards (6 * 56 cards).
   public static final int MAX_CARDS = 336;
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
      // Re-populate cards[] with the standard 56 numPacks cards; adjusted for
      // jokers.
      cards = new Card[numPacks * 56];

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

      // We start at the bottom of the card[] array (position 56 card
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
      char[] cardValues = new char[]{'A', '2', '3', '4', '5', '6', '7', '8',
            '9', 'T', 'J', 'Q', 'K',
            'X'}; // Adjusted for the joker in the master pack
      int insertPosition = 0;
      // The card suits multiplied by the card values should give us 56, a
      // standard deck with joker adjustment.
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

   /**
    * Add a card to the deck.
    *
    * @param card card to add to the deck
    *
    * @return true if card was added to the deck, false otherwise
    */
   public boolean addCard(Card card)
   {
      int occurrences = 0;
      for (Card cardInHand : cards)
      {
         if (cardInHand != null && cardInHand.equals(card))
         {
            occurrences++;
         }
      }
      if (occurrences >= numPacks || topCard >= cards.length)
      {
         // Return false if there are too many instances of the card in the
         // deck or there are too many cards in this hand.
         return false;
      }
      cards[topCard] = card;
      return true;
   }

   /**
    * Remove a card from the deck.
    *
    * @param card the card to remove from the deck
    *
    * @return true if card was in the deck and removed from it, false otherwise
    */
   public boolean removeCard(Card card)
   {
      for (int i = 0; i < this.getNumCards(); i++)
      {
         if (card.equals(cards[i]))
         {
            // When we find a matching card, direct the matching card's
            // position to the current top card, set the top card position to
            // empty, and decrement the position of the top card.
            cards[i] = cards[topCard];
            cards[topCard] = null;
            topCard--;
            return true;
         }
      }
      return false;
   }

   /**
    * Sort cards in the deck.
    */
   public void sort()
   {
      Card.arraySort(cards, cards.length);
   }

   /**
    * Getter for returning the number of cards remaining in the deck
    *
    * @return numberOfCards remaining
    */
   public int getNumCards()
   {
      int numberOfCards = 0; // Reset card counter to zero.
      // Count cards in the deck. There is a card in the deck if the space
      // for the card is not null.
      for (Card card : cards)
      {
         if (card != null)
         {
            numberOfCards++;
         }
      }
      return numberOfCards;
   }
}

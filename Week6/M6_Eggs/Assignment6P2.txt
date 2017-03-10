import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Programming Assignment 6, Part 2: Multithreading Implementation
 * School: CSU, Monterey Bay
 * Course: CST 338 Software Design
 * Professor: Jesse Cecil, MS
 *
 * Run "High Card" game with a timer that uses multithreading.
 *
 * @author Mariya Eggensperger
 * @author Kenneth Vader
 * @author Brittany Mazza
 */
public class Assignment6P2
{
   /**
    * Run the game "High Card" from the command line to begin a new game GUI.
    *
    * @param args command line argument - none expected
    */
   public static void main(String[] args)
   {
      // All classes are initialized once here instead of within the
      // constructors to ensure we are only using one instance of each and
      // avoid human error.
      CardGameView myGameView = new CardGameView();
      CardGameModel myGameModel = new CardGameModel(myGameView);
      TimerView timerView = new TimerView();
      Timer myGameTimer = new Timer(timerView);
      CardGameController myGameController = new CardGameController(myGameModel,
            myGameTimer);
      // Begin the timer and card game.
      myGameController.beginTimer();
      myGameController.play();
   }
}

class CardGameView
{
   // Create new storage for JLabels and JButtons
   private static JLabel[] computerLabels;
   private static JLabel[] humanLabels;
   private static JLabel[] playedCardLabels;
   private static JButton[] humanButtons;

   /**
    * Create a new card game table in the user's view.
    *
    * @param cardTable the card game table to initialize
    * @param humanHand the human hand to add to the card game table
    * @param restartGameListener the listener to restart the card game
    * @param playCardListener the listener to play a particular card from the
    *                         human hand
    * @param numCardsPerHand the number of cards each hand will have
    * @param numPlayers the number of players in the game
    * @return true if the game was initialized successfully, false otherwise
    */
   public boolean initializeCardGameTable(CardTable cardTable, Hand humanHand,
         ActionListener restartGameListener,
         ActionListener playCardListener,
         int numCardsPerHand, int numPlayers)
   {
      cardTable.setSize(800, 700);
      cardTable.setLocationRelativeTo(null);
      cardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Create Labels
      GUICard.loadCardIcons();
      computerLabels = new JLabel[numCardsPerHand];
      humanLabels = new JLabel[numCardsPerHand];
      humanButtons = new JButton[numCardsPerHand];
      // Play Area
      playedCardLabels = new JLabel[(numPlayers + 1) * 2];
      // Computer's played card goes in index 0
      playedCardLabels[0] = new JLabel(GUICard.getBackCardIcon());
      // Win/Loss messages go in index 1
      playedCardLabels[1] = new JLabel("Let's Play", JLabel.CENTER);
      // Human played card goes in index 2
      playedCardLabels[2] = new JLabel(GUICard.getBackCardIcon());
      // Computer name string goes in index 3
      playedCardLabels[3] = new JLabel("Computer", JLabel.CENTER);
      // Index 4 is used for the restart button, we leave it as an empty string
      playedCardLabels[4] = new JLabel("");
      // Human name string goes in index 5
      playedCardLabels[5] = new JLabel("You", JLabel.CENTER);
      // Computer labels
      for (int k = 0; k < numCardsPerHand; k++)
      {
         Icon tempIcon = GUICard.getBackCardIcon();
         computerLabels[k] = new JLabel(tempIcon);
      }
      for (int k = 0; k < numCardsPerHand; k++)
      {
         Icon tempIcon = GUICard.getIcon(humanHand.inspectCard(k));
         humanLabels[k] = new JLabel(tempIcon);
         // Add buttons below human player's card.
         humanButtons[k] = new JButton("Play Card #" + (k + 1));
         humanButtons[k].setActionCommand("" + k);
         humanButtons[k].addActionListener(playCardListener);
      }
      // Reset game button.
      JButton restartButton = new JButton("Restart");
      restartButton.addActionListener(restartGameListener);

      // Add Labels to Panels
      // Play Area
      for (int k = 0; k < playedCardLabels.length; k++)
      {
         if (k == 4)
         {
            cardTable.pnlPlayArea.add(restartButton);
         }
         else
         {
            cardTable.pnlPlayArea.add(playedCardLabels[k]);
         }
      }
      // Human Hand
      for (int k = 0; k < numCardsPerHand; k++)
      {
         cardTable.pnlHumanHand.add(humanLabels[k]);
      }
      // Human Buttons
      for (int k = numCardsPerHand; k < numCardsPerHand * 2; k++)
      {
         cardTable.pnlHumanHand.add(humanButtons[k - numCardsPerHand]);
      }
      // Computer Hand
      for (int k = 0; k < numCardsPerHand; k++)
      {
         cardTable.pnlComputerHand.add(computerLabels[k]);
      }

      // Show everything to user
      cardTable.setVisible(true);
      return true;
   }

   /**
    * Hide the specified human card in their hand to simulate playing a card.
    * This will also hide the associated button to play the card.
    *
    * @param playerHandIndex the index of the card to hide
    * @return true if the card was hidden successfully, false otherwise
    */
   public boolean hideHumanCard(int playerHandIndex)
   {
      // Hide the human's selected card from their hand.
      humanLabels[playerHandIndex].setVisible(false);
      humanButtons[playerHandIndex].setVisible(false);
      return true;
   }

   /**
    * Hide the next computer card in their hand to simulate playing a card.
    *
    * @return true if the card was hidden correctly, false otherwise
    */
   public boolean hideNextComputerCard()
   {
      // Hide the last displayed card in the computer's hand.
      for (int k = computerLabels.length - 1; k >= 0; k--)
      {
         if (computerLabels[k].isVisible())
         {
            computerLabels[k].setVisible(false);
            return true;
         }
      }
      return false;
   }

   /**
    * Display the status message to the user.
    *
    * @param message the status message to display
    * @return true if the status message was displayed correctly, false
    * otherwise
    */
   public boolean updateStatusMessage(String message)
   {
      if (message == null)
      {
         playedCardLabels[1].setText("Error encountered");
         return false;
      }
      playedCardLabels[1].setText(message);
      return true;
   }

   /**
    * Update the view for the card that was played by the human.
    *
    * @param cardPlayed the card played by the human
    * @return true if the card was shown correctly, false otherwise
    */
   public boolean showHumanPlayedCard(Card cardPlayed)
   {
      if (playedCardLabels == null || playedCardLabels[2] == null)
      {
         return false;
      }
      playedCardLabels[2].setIcon(GUICard.getIcon(cardPlayed));
      return true;
   }

   /**
    * Update the view for the card that was played by the computer.
    *
    * @param cardPlayed the card played by the computer
    * @return true if the card was shown correctly, false otherwise
    */
   public boolean showComputerPlayedCard(Card cardPlayed)
   {
      if (playedCardLabels == null || playedCardLabels[0] == null)
      {
         return false;
      }
      playedCardLabels[0].setIcon(GUICard.getIcon(cardPlayed));
      return true;
   }

   /**
    * Helper method to reset GUI when restart button is used.
    *
    * @param humanHand the human hand of cards
    * @param numCardsPerHand the maximum number of cards per hand
    */
   public void resetBoard(Hand humanHand, int numCardsPerHand)
   {
      // Reset playArea to initial state
      playedCardLabels[0].setIcon(GUICard.getBackCardIcon());
      playedCardLabels[2].setIcon(GUICard.getBackCardIcon());
      playedCardLabels[1].setText("Let's Play Again!");

      //Reset human and computer GUI Cards
      Icon tempIcon;
      for (int k = 0; k < numCardsPerHand; k++)
      {
         tempIcon = GUICard.getBackCardIcon();
         computerLabels[k].setIcon(tempIcon);
      }
      // Human Hand
      for (int k = 0; k < numCardsPerHand; k++)
      {
         tempIcon = GUICard.getIcon(humanHand.inspectCard(k));
         humanLabels[k].setIcon(tempIcon);
      }

      // Display cards and buttons
      for (int i = 0; i < numCardsPerHand; i++)
      {
         humanLabels[i].setVisible(true);
         humanButtons[i].setVisible(true);
         computerLabels[i].setVisible(true);
      }
   }
}

class CardGameModel
{
   static int NUM_CARDS_PER_HAND = 7;
   static int NUM_PLAYERS = 2;
   private static Card[] winnings;
   private static Card[] losings;
   private static int wonCards = 0;
   private static int computerWonCards = 0;
   private static CardGameFramework highCardGame = null;
   private CardGameView cardGameView;

   /**
    * Create a new card game model.
    *
    * @param cardGameView the view for the card game model
    */
   public CardGameModel(CardGameView cardGameView)
   {
      this.cardGameView = cardGameView;
   }

   /**
    * Setup the card game. This will take care of creating the entire board
    * and initializing the "High Card" game itself.
    *
    * @param restartGameListener the listener for when a request is made to
    *                            restart the game
    * @param playCardListener the listener for when a card is played from the
    *                         human hand
    * @return true if the card game was initialized, false otherwise
    */
   public boolean initializeCardGame(ActionListener restartGameListener,
         ActionListener playCardListener)
   {
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;
      CardTable cardTable = new CardTable("CardTable",
            NUM_CARDS_PER_HAND, NUM_PLAYERS);

      winnings = new Card[NUM_CARDS_PER_HAND];
      losings = new Card[NUM_CARDS_PER_HAND];
      highCardGame = new CardGameFramework(numPacksPerDeck, numJokersPerPack,
            numUnusedCardsPerPack, unusedCardsPerPack, NUM_PLAYERS,
            NUM_CARDS_PER_HAND);

      dealCards();

      cardGameView.initializeCardGameTable(cardTable, highCardGame.getHand(1),
            restartGameListener, playCardListener, NUM_CARDS_PER_HAND,
            NUM_PLAYERS);
      return true;
   }

   /**
    * Play a round of "High Card" with the card index provided for the human
    * hand.
    *
    * @param playerHandIndex the index for the card to play from the human hand
    * @return true if round was played successfully, false otherwise
    */
   public boolean playRound(int playerHandIndex)
   {

      Hand computerHand = highCardGame.getHand(0);
      Hand humanHand = highCardGame.getHand(1);

      // Get both cards
      // The computer will just play their next card
      Card computerCard = computerHand.playCard();
      Card humanCard = highCardGame.playCard(1, playerHandIndex);

      // Determine winner/loser status
      String outcomeMessage;
      boolean isTie = computerCard.getValue() == humanCard.getValue();
      if (isTie)
      {
         outcomeMessage = "You TIED this round!";
      }
      else if (humanCardIsHigher(computerCard, humanCard))
      {
         winnings[wonCards] = humanCard;
         wonCards++;
         outcomeMessage = "You WON this round!";
      }
      else
      {
         losings[computerWonCards] = humanCard;
         computerWonCards++;
         outcomeMessage = "You LOST this round!";
      }

      if (computerHand.getNumCards() <= 0 || humanHand.getNumCards() <= 0)
      {
         // When either player is out of cards, display if the user won and
         // how many cards they won total.
         outcomeMessage = "You won " + wonCards + " cards and lost "
               + computerWonCards + ". ";
         if (wonCards == computerWonCards)
         {
            outcomeMessage += "You TIED!";
         }
         else if (wonCards > computerWonCards)
         {
            outcomeMessage += "You WIN!";
         }
         else
         {
            outcomeMessage += "You LOST!";
         }
      }
      // Update views to show what happened in this round.
      cardGameView.updateStatusMessage(outcomeMessage);
      cardGameView.showHumanPlayedCard(humanCard);
      cardGameView.hideHumanCard(playerHandIndex);
      cardGameView.showComputerPlayedCard(computerCard);
      cardGameView.hideNextComputerCard();
      return true;
   }

   /**
    * Reset the game to prepare for a new game.
    */
   public void resetGame()
   {
      computerWonCards = 0;
      wonCards = 0;
      // Clear the set of winning cards.
      for (int i = 0; i < winnings.length; i++)
      {
         winnings[i] = null;
      }
      // Clear the set of losing cards.
      for (int i = 0; i < losings.length; i++)
      {
         losings[i] = null;
      }
      highCardGame.newGame();
      dealCards();
      cardGameView.resetBoard(highCardGame.getHand(1), NUM_CARDS_PER_HAND);
   }

   /**
    * Deal cards for the high card game.
    *
    * @return if the cards were dealt successfully, false otherwise
    */
   private boolean dealCards()
   {
      if (highCardGame.deal())
      {
         highCardGame.sortHands();
         return true;
      }
      return false;
   }

   /**
    * Helper method for the game "High Card". Return true if the human hand is
    * higher than the computer hand, false otherwise.
    *
    * @param computerCard the card played from the computer's hand
    * @param humanCard the card played from the human's hand
    * @return if the card played from the human hand is greater in value than
    * the card played from the computer's hand
    */
   private boolean humanCardIsHigher(Card computerCard, Card humanCard)
   {
      int computerCardRankIndex = 0;
      int humanCardRankIndex = 0;
      for (int i = 0; i < Card.valueRanks.length; i++)
      {
         // Get the rank index for the computer card.
         if (computerCard.getValue() == Card.valueRanks[i])
         {
            computerCardRankIndex = i;
         }
         // Get the rank index for the human card.
         if (humanCard.getValue() == Card.valueRanks[i])
         {
            humanCardRankIndex = i;
         }
      }
      return humanCardRankIndex > computerCardRankIndex;
   }
}

class CardGameController
{
   private CardGameModel cardGameModel;
   private Timer timer;

   /**
    * Create a new game controller. This will not immediately being a game.
    * The method "play" should be called to begin a new game.
    *
    * @param cardGameModel the model for the card game
    * @param timer the model for the game timer
    */
   public CardGameController(CardGameModel cardGameModel, Timer timer)
   {
      this.cardGameModel = cardGameModel;
      this.timer = timer;
   }

   /**
    * Initialize a new timer that will run along side the game.
    *
    * @return true if the timer started successfully, false otherwise
    */
   public boolean beginTimer()
   {
      ActionListener startTimerListener = new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            if (!timer.isThreadRunning())
            {
               timer.startThread();
            }
            timer.startOrStop();
         }
      };
      return timer.setTimerStartStopListener(startTimerListener);
   }

   /**
    * Initialize a new game and begin play.
    *
    * @return true if the game began as expected, false otherwise
    */
   public boolean play()
   {
      // Play card game
      ActionListener restartButtonListener = new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            cardGameModel.resetGame();
         }
      };
      ActionListener playCardListener = new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            // Play round
            int playerHandIndex = Integer.parseInt(e.getActionCommand());
            cardGameModel.playRound(playerHandIndex);
         }
      };
      return cardGameModel.initializeCardGame(restartButtonListener,
            playCardListener);
   }
}

class TimerView extends JFrame
{
   private static JLabel timerLabel;
   private static JButton startStopButton;

   /**
    * Create a new timer view.
    */
   public TimerView()
   {
      super();
      setTitle("timer");
      setSize(200, 150);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Note: Not setting the location will fix the timer at the upper left
      // hand corner of the screen.

      // The timer view will have two rows and one column. The first row will
      // have the timer and the second will have the "start/stop" button.
      GridLayout layout = new GridLayout(2, 1);

      // Create entire timer frame.
      JPanel timerPanel = new JPanel();
      timerPanel.setBorder(BorderFactory.createTitledBorder("Timer"));

      // Create timer text label.
      timerLabel = new JLabel();
      timerLabel.setText("0:00.00");
      timerPanel.add(timerLabel);

      // Create start button.
      startStopButton = new JButton("Start/Stop");

      // Add start and stop buttons.
      JPanel timerButtons = new JPanel();
      timerButtons.add(startStopButton);

      // Assemble labels and buttons on entire frame.
      setLayout(layout);
      add(timerPanel);
      add(timerButtons);

      setVisible(true);
   }

   /**
    * Update the timer display based on the seconds specified.
    *
    * @param hundredthsSecondsPassed tenths of a second to display on the timer
    *                                display
    *
    * @return true if the time was set correctly, false otherwise
    */
   public boolean updateTimer(int hundredthsSecondsPassed)

   {
      int minutes = (hundredthsSecondsPassed / 100) / 60;
      int seconds = (hundredthsSecondsPassed / 100) % 60;
      int hundredthsSeconds = hundredthsSecondsPassed % 100;
      timerLabel.setText(
            minutes + ":"
                  + String.format("%02d", seconds) + "."
                  + String.format("%02d", hundredthsSeconds));
      return true;
   }

   /**
    * Set the start button listener on the start button.
    *
    * @param listener the listener that will take action when the start button
    *                 is pressed
    *
    * @return true if the listener was set correctly, false otherwise
    */
   public boolean setStartStopButtonListener(ActionListener listener)
   {
      startStopButton.addActionListener(listener);
      return true;
   }
}

class Timer implements Runnable
{
   private static int INTERVAL = 10; // 10/1000 is 1/100th of a second.
   private TimerView view;
   private int hundredthsSecondsPassed = 0;
   private boolean isThreadRunning = false;
   private boolean isRunning = false;

   /**
    * Create a new timer.
    *
    * @param timerView the view for this new timer
    */
   public Timer(TimerView timerView)
   {
      this.view = timerView;
   }

   /**
    * Start the timer in a new thread.
    */
   public void startThread()
   {
      // Begin timer in separate thread and note that thread is running.
      Thread timerThread = new Thread(this);
      isThreadRunning = true;
      timerThread.start();
   }

   /**
    * Run the timer thread.
    */
   @Override
   public void run()
   {
      // With the timer is running
      while (isThreadRunning)
      {
         // Pause is int 100
         doNothing(INTERVAL);
         // If the timer is running
         if (isRunning)
         {
            // Increment hundredths Seconds Passed
            hundredthsSecondsPassed++;
            // Update view in ther timer application
            view.updateTimer(hundredthsSecondsPassed);
         }
      }

   }

   /**
    * Initialize the timer with start and stop button actions.
    *
    * @param startTimerListener the action to perform when the start/stop button
    *                           is pressed
    *
    * @return true if the timer was started successfully, false otherwise
    */
   public boolean setTimerStartStopListener(ActionListener startTimerListener)
   {
      return view.setStartStopButtonListener(startTimerListener);
   }

   /**
    * Start or stop the timer. If the timer was set already set, it will reset
    * the timer. If the time was already running, stop it.
    *
    * @return true if the action was performed successfully, false otherwise
    */
   public boolean startOrStop()
   {
      if (isRunning)
      {
         // If timer is running, pause it.
         isRunning = false;
         return true;
      }
      // If timer is not running, start it at 0 again.
      hundredthsSecondsPassed = 0;
      isRunning = true;
      return view.updateTimer(hundredthsSecondsPassed);
   }

   /**
    * Get if the thread is already running or not.
    *
    * @return true if the thread is running, false otherwise
    */
   public boolean isThreadRunning()
   {
      return isThreadRunning;
   }

   /**
    * Do nothing for a specified number of milliseconds. This will pause the
    * thread, allowing time to pass.
    *
    * @param milliseconds the number of milliseconds to wait
    *
    * @return true if it did nothing for a specified number of milliseconds as
    * expected, false otherwise
    */
   private boolean doNothing(int milliseconds)
   {
      try
      {
         Thread.sleep(milliseconds); // 10 milliseconds
         return true;
      }
      catch (InterruptedException e)
      {
         isThreadRunning = false;
         System.out.println("Unexpected interrupt");
         System.exit(0);
      }
      return false;
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
      GridLayout playerHandLayout = new GridLayout(2, numCardsPerHand);
      pnlHumanHand = new JPanel();
      pnlHumanHand.setBorder(playerHandBorder);
      pnlHumanHand.setLayout(playerHandLayout);
      // Create playing area panel.
      Border playingAreaBorder = BorderFactory.createTitledBorder(
            "Playing Area");
      GridLayout playAreaLayout = new GridLayout(2, 3);
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
      for (int i = myCards.length - 1; i >= 0; i--)
      {
         if (myCards[i] != null)
         {
            // Get and remove the card in highest position of the hand.
            Card playedCard = myCards[i];
            myCards[i] = null;
            numCards--;
            return playedCard;

         }
      }
      // If a card was not found, return a bad card.
      Card badCard = new Card();
      badCard.set(' ', null); // Intentionally set errorFlag on card.
      return badCard;
   }

   /**
    * Play a card from a particular place in the hand.
    *
    * @param cardIndex the card index for the card to play
    * @return the card at the specified index
    */
   public Card playCard(int cardIndex)
   {
      if (numCards > 0 && cardIndex < myCards.length
            && myCards[cardIndex] != null)
      {
         // Get and remove the card in the specified location.
         Card playedCard = myCards[cardIndex];
         myCards[cardIndex] = null;
         numCards--;
         return playedCard;
      }
      // Return a bad card if there aren't any more cards in the hand or
      // if the requested card doesn't exist.
      Card badCard = new Card();
      badCard.set(' ', null); // Intentionally set errorFlag on card.
      return badCard;
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
      if (topCard < 1)
      {
         // Return null if there aren't any more cards in the deck.
         return null;
      }
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

//
// Note:
// While we're using this class (CardGameFramework), we did not actually
// write it. It is copied and pasted from:
// http://www.siskiyous.edu/class/csci1007/cecil/cardGameFramework.txt
//
// class CardGameFramework  ----------------------------------------------------
class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks;            // # standard 52-card packs per deck
   // ignoring jokers or unused cards
   private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack;  // # cards removed from each pack
   private int numCardsPerHand;        // # cards to deal each player
   private Deck deck;               // holds the initial full deck and gets
   // smaller (usually) during play
   private Hand[] hand;             // one Hand for each player
   private Card[] unusedCardsPerPack;   // an array holding the cards not used
   // in the game.  e.g. pinochle does not
   // use cards 2-8 of any suit

   public CardGameFramework( int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if  (numCardsPerHand < 1 ||
            numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
            / numPlayers )
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameFramework()
   {
      this(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   public Card getCardFromDeck() { return deck.dealCard(); }

   public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }

   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard( unusedCardsPerPack[k] );

      // add jokers
      for (k = 0; k < numPacks; k++)
         for ( j = 0; j < numJokersPerPack; j++)
            deck.addCard( new Card('X', Card.Suit.values()[j]) );

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards ; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard( deck.dealCard() );
            else
            {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }

   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
            cardIndex < 0 || cardIndex > numCardsPerHand - 1)
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);
      }

      // return the card played
      return hand[playerIndex].playCard(cardIndex);

   }


   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;

      // Are there enough Cards?
      if (deck.getNumCards() <= 0)
         return false;

      return hand[playerIndex].takeCard(deck.dealCard());
   }

}

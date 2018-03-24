/*
 *----------------------------------------------------------------
 *
 * Project Name: War Card Game
 *----------------------------------------------------------------
 * Description:(Class overview)
 *  A program designed to use a Deck API to build a functioning
 * version of the card game War.
 *  
 */
package us.wc.war;

import java.util.ArrayList;

import us.wc.deck.Card;
import us.wc.deck.CardFactory;
import us.wc.deck.Deck;

/**
 * A simple version of the card game WarGame
 * 
 */
public class WarGame {

	public static boolean isDiscardPileEmpty(Deck deck) {

		return (deck.getDiscardPileSize() == 0) ? true : false;
	}

	public static int reshuffle(Deck deck) {
		deck.shuffle();

		// return -1 if there are no cards left to reshuffle
		// else, return zero
		return (deck.getDrawPileSize() == 0) ? -1 : 0;
	}

	public static void cardStats(Deck deck1, Deck deck2) {

		System.out.printf("%-15s %-15s %-15s %-15s\n ", "Player", "Draw Pile",
				"Discard Pile", "Num cards");
		System.out.printf("%-15s %-15d %-15d %-15d\n ", "Player1",
				deck1.getDrawPileSize(), deck1.getDiscardPileSize(),
				deck1.getNumCards());
		System.out.printf("%-15s %-15d %-15d %-15d\n ", "Player2",
				deck2.getDrawPileSize(), deck2.getDiscardPileSize(),
				deck2.getNumCards());
	}

	public static void addToWinnersPile(ArrayList<Card> cards, Deck winnersDeck) {

		for (Card c : cards) {
			winnersDeck.addToDiscard(c);
		}

	}

	public static int checkValue(Card card1, Card card2) {

		int winner = 0;
		if (card1.getOrdinal() > card2.getOrdinal()) {
			winner = 1;
		} else if (card1.getOrdinal() < card2.getOrdinal()) {
			winner = 2;
		}

		return winner;
	}

	public static void playRound(Deck deck1, Deck deck2) {

		Card card1;
		Card card2;
		int winner = 0;
		boolean over = true;

		System.out.println("\n***New Round***");

		if (deck1.getDrawPileSize() == 0) {
			if (reshuffle(deck1) == -1) {
				System.out.println("Player 1 is out of cards!");
				return;
			}
		}

		if (deck2.getDrawPileSize() == 0) {
			if (reshuffle(deck2) == -1) {
				System.out.println("Player 2 is out of cards!");
				return;
			}
		}

		card1 = deck1.draw();
		card2 = deck2.draw();

		System.out.println("Player 1's " + card1.toString() + " Vs Player 2's "
				+ card2.toString());

		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(card1);
		cards.add(card2);

		// call a function that finds out which card has the higher value, save
		// that result
		winner = checkValue(card1, card2);

		// if the cards have the same value
		if (winner == 0) {
			// keep track of number of cards involved in this round,
			// starting at index 0 there are currently 2
			int count = 1;
			System.out.println("Tie! Begin War");

			do {
				// save the cards from a tie round into an array
				for (int i = 0; i < 2; i++) {
					if (isDiscardPileEmpty(deck1)) {
						reshuffle(deck1);
					}

					if (isDiscardPileEmpty(deck2)) {
						reshuffle(deck2);
					}

					cards.add(deck1.draw());
					cards.add(deck2.draw());

					count = count + 2;

					System.out.println(i + ": Player 1's "
							+ cards.get(count - 1).toString()
							+ " Vs Player 2's "
							+ cards.get(count).toString().toString());
				}

				if (cards.get(count - 1).getOrdinal() > cards.get(count)
						.getOrdinal()) {
					winner = 1;
				} else if (cards.get(count - 1).getOrdinal() < cards.get(count)
						.getOrdinal()) {
					winner = 2;
				} else {
					over = false;
				}
			}// repeat the loop if another tie occurs
			while (over != true);
		}

		System.out.println("winner: " + winner);
		// call the function with the list of cards and the deck of the winner
		addToWinnersPile(cards, ((winner == 1) ? deck1 : deck2));

		cardStats(deck1, deck2);

	}

	/**
	 * This function when called will play an entire game of War. playRound will
	 * get called until either play is completely out of cards.
	 * 
	 * @param player1
	 *            Deck of cards representing player 1
	 * @param player2
	 *            Deck of cards representing player 2
	 */
	public static void playGame(Deck player1, Deck player2) {
		while (player1.getNumCards() > 0 && player2.getNumCards() > 0)
			playRound(player1, player2);

		System.out.println("Player1: " + player1.getNumCards());
		System.out.println("Player2: " + player2.getNumCards());
		if (player1.getNumCards() > 0)
			System.out.println("Player 1 Wins!");
		else if (player2.getNumCards() > 0)
			System.out.println("Player 2 Wins!");
	}

	public static void main(String[] args) {

		// Get a new CardFactory instance
		CardFactory factory = new CardFactory();

		// Get a full deck, set as player 1
		Deck player1 = factory.createFullDeck();
		// Make a new empty deck for player 2
		Deck player2 = new Deck();
		// Shuffle the player 1 deck
		player1.shuffle();

		// deal half of player 1's shuffled deck to player 2
		int deckSize = player1.getDrawPileSize() / 2;
		for (int i = 0; i < deckSize; ++i)
			player2.addToDiscard(player1.draw());

		// shuffle both decks
		player1.shuffle();
		player2.shuffle();

		// play a single round of War

		for (int i = 0; i < 90; i++) {
			playRound(player1, player2);
		}

		// play an entire game of War
		// playGame(player1, player2);
	}
}

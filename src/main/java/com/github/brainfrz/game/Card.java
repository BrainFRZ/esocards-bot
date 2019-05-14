package com.github.brainfrz.game;

public class Card {
    public enum Face {
        CHAMPION, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, DAGGER, ARMOR, SWORD;

        public int value() {
            if (this == CHAMPION) {
                return 11; // Ace equivalent
            } else  if (this.ordinal() >= TWO.ordinal() && this.ordinal() <= TEN.ordinal())
                return this.ordinal() + 1; // Numbered card
            else {
                return 10; // Face card
            }
        }

        public String faceName() {
            int cardNumber = this.ordinal() + 1;

            if (cardNumber >= 2 && cardNumber <= 10) {
                return Integer.toString(cardNumber);
            } else {
                return this.name().substring(0, 1);
            }
        }

        @Override
        public String toString() {
            return this.name().substring(0,1).toUpperCase() + this.name().substring(1).toLowerCase();
        }
    }

    public enum Suit {
        MOLAG_BAL, MARA, ZENITHAR, SANGUINE;

        @Override
        public String toString() {
            switch (this) {
                case MOLAG_BAL: return "Molag Bal"; // Spade
                case MARA:      return "Mara";      // Heart
                case ZENITHAR:  return "Zenithar";  // Diamond
                case SANGUINE:  return "Sanguine";  // Club
                default:        return "Unknown";
            }
        }
    }


    public final Face face;
    public final Suit suit;

    public Card(final Face face, final Suit suit) {
        this.face = face;
        this.suit = suit;
    }


    public int value() {
        return this.face.value();
    }
}

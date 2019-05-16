package com.github.brainfrz.game;

public class Card {
    public enum Face {
        CHAMPION, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, DAGGER, ARMOR, SWORD;

        int value() {
            if (this == CHAMPION) {
                return 11; // Ace equivalent
            } else  if (this.ordinal() >= TWO.ordinal() && this.ordinal() <= TEN.ordinal())
                return this.ordinal() + 1; // Numbered card
            else {
                return 10; // Face card
            }
        }

        String faceName() {
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

    enum Suit {
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


    final Face face;
    final Suit suit;

    Card(final Face face, final Suit suit) {
        this.face = face;
        this.suit = suit;
    }


    int value() {
        return this.face.value();
    }


    @Override
    public String toString() {
        return (face + " of " + suit);
    }
}

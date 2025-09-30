package org.example.AtmMachine.AtmStates;

import org.example.AtmMachine.ATM;
import org.example.AtmMachine.Card;

public class IdleState extends ATMState {

    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card is inserted");
        atm.setCurrentATMState(new HasCardState());
    }
}


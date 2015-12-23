package modele;

import java.util.LinkedList;

public class CashRegister {
	private String aActualSession;
	public Cashier aCashier;
	public LinkedList<Session> aSession = new LinkedList<Session>();
	public Key aKey;

	public void login(String pSession) {
		throw new UnsupportedOperationException();
	}

	public void open(String pKey) {
		throw new UnsupportedOperationException();
	}
}
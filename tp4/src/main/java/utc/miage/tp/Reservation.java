package utc.miage.tp;

public record Reservation(
		Long id,
		String clientName,
		String region,
		String regionVisual,
		int personCount,
		String paymentMethod,
		String iban) {

	public boolean isBankTransfer() {
		return "Virement".equalsIgnoreCase(paymentMethod);
	}
}
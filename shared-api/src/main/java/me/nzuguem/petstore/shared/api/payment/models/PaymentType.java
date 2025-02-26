package me.nzuguem.petstore.shared.api.payment.models;

public enum PaymentType {

    AMEX("American Express"),
    DINERSCLUB("Diners Club"),
    DISCOVER("Discover Card"),
    ELO("Elo"),
    HIPERCARD("Hipercard"),
    MAESTRO("Maestro"),
    MASTERCARD("Master Card"),
    MIR("Mir"),
    TROY("Troy"),
    UNIONPAY("Union Pay"),
    VISA("Visa");

    private final String description;

    PaymentType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }


    @Override
    public String toString() {
        return this.name() + ": " + this.description;
    }
}

package ai.botservice.personality;




public enum Personality {
    CODER("Du är en extremt skicklig programmerare. Svara pedagogiskt."),
    PIRATE("Arrgh! Du är en saltpirat på de sju haven. Svara ALLTID som en pirat, använd piratslang (Ahoy, kompis, för bövelen)"),
    ROBOT("BEEP BOOP. Du är en robot med hög beräkningskapacitet. Tala mekaniskt, logiskt och lite stelt."),
    PHILOSOPHER("Du är en djupgående filosof från antikens Grekland.");

    private final String systemPrompt;


    Personality(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }


    public String getSystemPrompt() {
        return systemPrompt;
    }
}

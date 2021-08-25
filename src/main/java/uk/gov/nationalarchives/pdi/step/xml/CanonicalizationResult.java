package uk.gov.nationalarchives.pdi.step.xml;

public class CanonicalizationResult {

    private final String canonicalXml;
    private String errorMessage = "";
    private boolean hasError = false;

    public CanonicalizationResult(final String canonicalXml) {
        this.canonicalXml = canonicalXml;
    }

    public String getCanonicalXml() {
        return canonicalXml;
    }

    public boolean hasError() {
        return hasError;
    }

    public void setHasError(final boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

}

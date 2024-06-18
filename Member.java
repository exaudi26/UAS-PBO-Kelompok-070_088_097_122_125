class Member {
    private String id;
    private String name;
    private int loanCount;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.loanCount = 0; // Initialize loan count to 0
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLoanCount() {
        return loanCount;
    }

    public void incrementLoanCount() {
        loanCount++;
    }

    public void decrementLoanCount() {
        if (loanCount > 0) {
            loanCount--;
        }
    }

    public boolean hasActiveLoans() {
        return loanCount > 0;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Loan Count: " + loanCount;
    }
}
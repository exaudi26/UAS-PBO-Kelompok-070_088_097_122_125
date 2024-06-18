class Loan {
    private String bookId;
    private String memberId;
    private String date;
    private String returnDate;
    private int period;

    public Loan(String bookId, String memberId, String date, String returnDate, int period) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.date = date;
        this.returnDate = returnDate;
        this.period = period;
    }

    public String getBookId() {
        return bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getDate() {
        return date;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public int getPeriod() {
        return period;
    }
}
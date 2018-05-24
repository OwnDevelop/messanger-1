package domain;

public class MessageWithUnreadDTO extends MessageDTO {
    private Integer countUnread;

    public Integer getCountUnread() {
        return countUnread;
    }

    public void setCountUnread(Integer countUnread) {
        this.countUnread = countUnread;
    }
}

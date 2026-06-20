package group3.paws_hope.dto.req;

public class EmailReq {
    private String to;
    private String subject;
    private String content;

    // Getter và Setter (Hoặc dùng @Data nếu có Lombok)
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}

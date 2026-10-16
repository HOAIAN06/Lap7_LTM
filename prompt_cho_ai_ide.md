# NGỮ CẢNH DỰ ÁN (LAB 6 - MAIL SERVER/CLIENT)
Đây là dự án ứng dụng Mail đơn giản bằng Java.
Yêu cầu cốt lõi: Đăng ký, Đăng nhập, Gửi email (lưu vào Database và gửi qua SMTP Gmail thực tế), Đọc email (POP3).

# YÊU CẦU KỸ THUẬT & QUY TẮC CODE (QUAN TRỌNG)
1.  **Ngôn ngữ:** Java (Core Java/Console App). KHÔNG dùng Spring Boot hay framework phức tạp.
2.  **Cơ sở dữ liệu:** Supabase (Sử dụng driver JDBC PostgreSQL).
3.  **Thư viện Mail:** `jakarta.mail` (hoặc `javax.mail`) cho SMTP và POP3.
4.  **Phong cách Code CHỈ ĐỊNH:**
    *   Đơn giản tuyệt đối, ngắn gọn nhất có thể.
    *   KHÔNG mã hóa mật khẩu (lưu plaintext để code đơn giản).
    *   KHÔNG tự sinh các phương thức getter/setter/toString không cần thiết (khuyến khích dùng `public` fields cho các class Model để truy xuất trực tiếp).
    *   Gom các chức năng chung vào 1-2 class để tránh rườm rà.

# CẤU TRÚC THƯ MỤC DỰ KIẾN (TỐI GIẢN)
```text
src/
├── Main.java                (Điểm chạy chương trình chính - Menu Console)
├── models/
│   ├── User.java            (Chỉ chứa public fields: username, password)
│   └── Mail.java            (Chỉ chứa public fields: sender, receiver, subject, content)
├── db/
│   └── SupabaseDB.java      (Xử lý mọi thao tác với CSDL)
└── network/
    └── MailProtocol.java    (Xử lý giao thức SMTP gửi Gmail và POP3)
```

# PHÂN CHIA QUY TRÌNH LÀM VIỆC (2 DEVELOPER)
AI hãy đóng vai trò hỗ trợ từng Developer theo các task độc lập sau để hoàn toàn không bị conflict:

## TÌNH HUỐNG LÀM VIỆC CỦA DEV 1: Xử lý Database & Model (Không đụng tới Mail)
**Nhiệm vụ của AI khi chat với Dev 1:**
1.  Tạo thư mục `models/` với 2 class `User.java` và `Mail.java` siêu đơn giản (public fields).
2.  Tạo thư mục `db/` với class `SupabaseDB.java`.
3.  Viết phương thức kết nối JDBC tới Supabase PostgreSQL.
4.  Viết phương thức: `registerUser(username, password)` (Insert thẳng, không băm mật khẩu).
5.  Viết phương thức: `loginUser(username, password)`.
6.  Viết phương thức: `saveMailToDb(Mail mail)` (Lưu lịch sử thư vào DB).
*(Dev 1 chỉ làm việc trong thư mục `db` và `models`)*

## TÌNH HUỐNG LÀM VIỆC CỦA DEV 2: Xử lý Giao thức Mail & Main UI (Không đụng tới DB)
**Nhiệm vụ của AI khi chat với Dev 2:**
1.  Tạo thư mục `network/` với class `MailProtocol.java`.
2.  Viết phương thức `sendEmailViaGmail(Mail mail, String gmailAppPassword)` sử dụng SMTP.
3.  Viết phương thức `readEmails(String gmailUsername, String gmailAppPassword)` sử dụng POP3.
4.  Tạo file `Main.java` ngoài cùng:
    *   Tạo Console UI (dùng `Scanner`) với các menu: 1. Đăng ký, 2. Đăng nhập, 3. Gửi Mail, 4. Đọc Mail.
    *   Gọi các hàm từ `SupabaseDB` (của Dev 1) và `MailProtocol` (của Dev 2) để ghép nối logic.
*(Dev 2 chỉ làm việc ở `Main.java` và thư mục `network`)*

---
**Lệnh cho AI:** "Đọc hiểu file này. Nếu tôi nói 'Tôi là Dev 1', hãy sinh ngay toàn bộ code cho phần của Dev 1. Nếu tôi nói 'Tôi là Dev 2', hãy sinh toàn bộ code cho phần của Dev 2."
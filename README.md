# Spring Security Multi-Factor Authentication (MFA) Dem

Implementation of Multi-Factor Authentication using Spring Boot, Spring Security, and Time-based One-Time Password (TOTP) protocol. This project demonstrates how to add an extra layer of security to your web applications using authenticator apps like Google Authenticator, Authy, or Microsoft Authenticator.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Project Structure](#-project-structure)
- [How It Works](#-how-it-works)
- [Usage Guide](#-usage-guide)
- [API Endpoints](#-api-endpoints)
- [Security Considerations](#-security-considerations)
- [Troubleshooting](#-troubleshooting)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

- ✅ **Username/Password Authentication** - Traditional form-based login with Spring Security
- ✅ **TOTP-Based MFA** - Time-based One-Time Password implementation (RFC 6238)
- ✅ **QR Code Generation** - Easy setup by scanning QR codes with authenticator apps
- ✅ **Enable/Disable MFA** - Users can toggle MFA protection on their accounts
- ✅ **Session Management** - Proper session handling for MFA verification flow
- ✅ **CSRF Protection** - Built-in protection against Cross-Site Request Forgery
- ✅ **Responsive UI** - Clean Thymeleaf templates with modern styling
- ✅ **H2 Database Console** - Built-in database viewer for development

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Programming language |
| Spring Boot | 3.2.0 | Application framework |
| Spring Security | 6.x | Security framework |
| Spring Data JPA | 3.2.0 | Data persistence |
| Thymeleaf | 3.1.x | Template engine |
| H2 Database | 2.x | In-memory database (dev) |
| TOTP Library | 1.7.1 | TOTP generation/verification |
| ZXing | 3.5.2 | QR code generation |
| Lombok | 1.18.x | Boilerplate reduction |
| Maven | 3.6+ | Build tool |

## 📦 Prerequisites

Before running this application, ensure you have:

- **Java Development Kit (JDK) 17 or higher** - [Download](https://adoptium.net/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **IDE** - IntelliJ IDEA, Eclipse, or VS Code (optional but recommended)
- **Authenticator App** - Google Authenticator, Authy, Microsoft Authenticator, or any TOTP-compatible app

## 🚀 Installation

### 1. Clone or Download the Project

```bash
git clone https://github.com/michaeljuren/spring-boot-4-mfa.git
cd spring-security-mfa
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run directly from your IDE by executing the `MfaApplication` class.

### 4. Access the Application

Open your browser and navigate to:
```
http://localhost:8080
```

## 📁 Project Structure

```
spring-security-mfa/
├── src/
│   ├── main/
│   │   ├── java/com/example/mfa/
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java              # Spring Security configuration
│   │   │   │   └── DataInitializer.java             # Initial test data setup
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java              # Home and login endpoints
│   │   │   │   └── MfaController.java               # MFA setup and verification
│   │   │   ├── entity/
│   │   │   │   └── User.java                        # User entity with MFA fields
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java              # JPA repository interface
│   │   │   ├── security/
│   │   │   │   └── MfaAuthenticationSuccessHandler.java  # Custom auth handler
│   │   │   ├── service/
│   │   │   │   ├── TotpService.java                 # TOTP operations
│   │   │   │   └── UserService.java                 # User management
│   │   │   └── MfaApplication.java                  # Application entry point
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── home.html                        # Home dashboard
│   │       │   ├── login.html                       # Login page
│   │       │   ├── mfa-setup.html                   # MFA setup with QR code
│   │       │   └── mfa-verify.html                  # MFA code verification
│   │       └── application.properties               # Application configuration
│   └── test/
│       └── java/                                    # Unit and integration tests
├── pom.xml                                          # Maven dependencies
└── README.md                                        # This file
```

## 🔐 How It Works

### Authentication Flow

```
┌─────────────────────────────────────────────────────────────────┐
│  1. User enters username/password                               │
│     ↓                                                            │
│  2. Spring Security validates credentials                       │
│     ↓                                                            │
│  3. MfaAuthenticationSuccessHandler checks if MFA is enabled    │
│     ↓                                                            │
│  4a. MFA Disabled → Redirect to /home                           │
│  4b. MFA Enabled  → Redirect to /mfa/verify                     │
│     ↓                                                            │
│  5. User enters 6-digit TOTP code from authenticator app        │
│     ↓                                                            │
│  6. System verifies code against stored secret                  │
│     ↓                                                            │
│  7. Valid code → Grant full access → Redirect to /home          │
│     Invalid   → Show error → Stay on /mfa/verify                │
└─────────────────────────────────────────────────────────────────┘
```

### TOTP (Time-based One-Time Password)

- **Algorithm**: HMAC-SHA1
- **Digits**: 6
- **Time Step**: 30 seconds
- **Standard**: RFC 6238

The system generates a shared secret during MFA setup. This secret is stored in the database and also embedded in the QR code. The authenticator app uses this secret to generate time-synchronized codes that match what the server expects.

## 📖 Usage Guide

### First Time Login

1. **Navigate to the application**
   ```
   http://localhost:8080
   ```

2. **Login with default credentials**
   - Username: `user`
   - Password: `password`

3. **You'll be redirected to the home page** (MFA is disabled by default)

### Setting Up MFA

1. **Click "Enable MFA"** on the home page

2. **Scan the QR code** with your authenticator app:
   - Open Google Authenticator (or similar app)
   - Tap "+" or "Add account"
   - Choose "Scan a QR code"
   - Point your camera at the QR code on screen

3. **Alternative: Manual entry**
   - If you can't scan the QR code, manually enter the secret key shown below it
   - Choose "Time-based" or "TOTP" as the account type

4. **Save the secret key** somewhere secure (password manager recommended)

5. **Click "Done - Return to Home"**

### Testing MFA

1. **Log out** of the application

2. **Log in again** with the same credentials

3. **Enter the 6-digit code** from your authenticator app when prompted
   - The code changes every 30 seconds
   - Make sure your device time is synchronized

4. **Click "Verify"** to complete authentication

### Disabling MFA

1. **Log in to your account** (with MFA if enabled)

2. **Click "Disable MFA"** on the home page

3. **Confirm the action**

4. **MFA is now disabled** - subsequent logins won't require the code

## 🌐 API Endpoints

### Public Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Redirects to home |
| GET | `/login` | Login page |
| POST | `/login` | Process login |
| GET | `/logout` | Logout endpoint |
| GET | `/h2-console` | H2 database console |

### Protected Endpoints (Authentication Required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/home` | User dashboard |
| GET | `/mfa/setup` | MFA setup page with QR code |
| POST | `/mfa/disable` | Disable MFA for current user |

### MFA Verification Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/mfa/verify` | MFA code entry page |
| POST | `/mfa/verify` | Verify TOTP code |

## 🔒 Security Considerations

### Current Implementation (Development)

⚠️ **This is a demonstration project. For production use, implement the following:**

### Essential Production Changes

1. **Use a Persistent Database**
   ```properties
   # Replace H2 with PostgreSQL/MySQL
   spring.datasource.url=jdbc:postgresql://localhost:5432/mfadb
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   ```

2. **Encrypt MFA Secrets**
   ```java
   // Don't store secrets in plain text!
   // Use JPA AttributeConverter with encryption
   @Convert(converter = SecretEncryptionConverter.class)
   private String mfaSecret;
   ```

3. **Implement Rate Limiting**
   - Limit MFA verification attempts (5-10 per 15 minutes)
   - Lock account after repeated failures
   - Consider using Spring Security's account locking features

4. **Add Backup Codes**
   ```java
   // Generate 10 single-use backup codes during MFA setup
   // Store hashed versions in the database
   @OneToMany(mappedBy = "user")
   private List<BackupCode> backupCodes;
   ```

5. **Enable HTTPS**
   ```properties
   server.ssl.enabled=true
   server.ssl.key-store=classpath:keystore.p12
   server.ssl.key-store-password=${SSL_PASSWORD}
   server.ssl.key-store-type=PKCS12
   ```

6. **Implement "Remember Device" Feature**
   - Store trusted device tokens
   - Skip MFA for recognized devices (30-90 days)
   - Allow users to view and revoke trusted devices

7. **Add Security Headers**
   ```java
   http.headers()
       .contentSecurityPolicy("default-src 'self'")
       .frameOptions().deny()
       .xssProtection().block(true);
   ```

8. **Environment Variables**
   ```bash
   # Never hardcode sensitive values
   export DB_PASSWORD=your_secure_password
   export ENCRYPTION_KEY=your_encryption_key
   ```

### Best Practices Implemented

✅ **CSRF Protection** - All forms include CSRF tokens  
✅ **Password Hashing** - BCrypt with default strength  
✅ **Session Management** - Proper session attribute handling  
✅ **Input Validation** - Pattern matching for TOTP codes  
✅ **HttpOnly Cookies** - Protected from XSS attacks  

## 🐛 Troubleshooting

### Application Won't Start

**Problem**: Circular dependency error
```
The dependencies of some of the beans form a cycle
```

**Solution**: Ensure `@Lazy` annotation is added to SecurityConfig constructor:
```java
public SecurityConfig(@Lazy MfaAuthenticationSuccessHandler handler) {
    this.mfaAuthenticationSuccessHandler = handler;
}
```

### Login Returns 403 Forbidden

**Problem**: Missing CSRF token

**Solution**: Verify all forms include the CSRF hidden input:
```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
```

### QR Code Not Displaying

**Problem**: Image data not rendering

**Solutions**:
- Check browser console for errors
- Verify ZXing library is in classpath
- Ensure secret generation is working

### MFA Code Not Accepted

**Problem**: "Invalid MFA code" error

**Possible Causes**:
1. **Time Synchronization** - Device clock is off
   - Solution: Enable automatic time sync on your device
   
2. **Wrong Secret** - Secret wasn't saved correctly
   - Solution: Disable and re-enable MFA
   
3. **Code Expired** - Codes expire every 30 seconds
   - Solution: Enter a fresh code quickly

4. **Database Issue** - Secret not stored in DB
   - Solution: Check H2 console: `SELECT * FROM users`

### Cannot Access Home Page After Login

**Problem**: Redirect loop or access denied

**Solution**: Check that MFA verification flow is completing properly:
```java
// Verify session attributes are set correctly
session.setAttribute("mfa_authenticated", true);
```

### H2 Console Not Accessible

**Problem**: 403 or 404 on `/h2-console`

**Solution**: Add to SecurityConfig:
```java
.requestMatchers("/h2-console/**").permitAll()
```

And disable frame options for H2:
```java
.headers().frameOptions().sameOrigin()
```

## 🚧 Future Enhancements

### Planned Features

- [ ] **User Registration** - Allow new users to sign up
- [ ] **Email Verification** - Verify email addresses during registration
- [ ] **Password Reset** - Forgot password flow with email
- [ ] **Backup Codes** - One-time use codes for account recovery
- [ ] **SMS-Based MFA** - Alternative to TOTP using SMS
- [ ] **Remember Device** - Skip MFA on trusted devices
- [ ] **Security Audit Log** - Track authentication attempts and changes
- [ ] **Admin Dashboard** - Manage users and view MFA statistics
- [ ] **Multi-Tenant Support** - Support multiple organizations
- [ ] **OAuth2 Integration** - Social login (Google, GitHub, etc.)
- [ ] **WebAuthn Support** - Passwordless authentication with FIDO2
- [ ] **REST API** - JSON-based API for mobile apps
- [ ] **Docker Support** - Containerized deployment
- [ ] **Kubernetes Deployment** - Production-ready k8s manifests

### Code Quality Improvements

- [ ] Unit tests with JUnit 5 and Mockito
- [ ] Integration tests with @SpringBootTest
- [ ] Code coverage reports
- [ ] SonarQube integration
- [ ] CI/CD pipeline (GitHub Actions/Jenkins)
- [ ] API documentation with Swagger/OpenAPI

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork the repository**

2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Commit your changes**
   ```bash
   git commit -m 'Add some amazing feature'
   ```

4. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```

5. **Open a Pull Request**

### Code Style

- Follow standard Java naming conventions
- Use Lombok annotations to reduce boilerplate
- Add Javadoc comments for public methods
- Write unit tests for new features
- Ensure all tests pass before submitting PR

## 📚 Learning Resources

### Spring Security
- [Official Documentation](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture)
- [Baeldung Spring Security Tutorials](https://www.baeldung.com/security-spring)

### TOTP and MFA
- [RFC 6238 - TOTP Specification](https://datatracker.ietf.org/doc/html/rfc6238)
- [Google Authenticator](https://github.com/google/google-authenticator)
- [OWASP MFA Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multifactor_Authentication_Cheat_Sheet.html)

### General Security
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Spring Security Best Practices](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

Created as a demonstration project for learning Spring Security and Multi-Factor Authentication implementation.

## 🙏 Acknowledgments

- Spring Framework team for excellent documentation
- Sam Stevens for the TOTP library
- ZXing project for QR code generation
- The open-source community

---

## 📞 Support

For questions or issues:
1. Check the [Troubleshooting](#-troubleshooting) section
2. Review existing GitHub issues
3. Open a new issue with detailed information

## ⭐ Star This Project

If you find this project helpful, please consider giving it a star on GitHub!

---

**Happy Coding! 🚀**
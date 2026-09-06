---
config:
  theme: base
  themeVariables:
    primaryColor: "#E0F2FE"
    primaryBorderColor: "#0284C7"
    primaryTextColor: "#0F172A"
    secondaryColor: "#F1F5F9"
    tertiaryColor: "#FEF08A"
    lineColor: "#64748B"
    textColor: "#1E293B"
    actorBkg: "#0284C7"
    actorBorder: "#0369A1"
    actorTextColor: "#FFFFFF"
    signalColor: "#0284C7"
    signalTextColor: "#334155"
    labelBoxBkgColor: "#F8FAFC"
    labelBoxBorderColor: "#CBD5E1"
    labelTextColor: "#334155"
    noteBkgColor: "#FEF9C3"
    noteBorderColor: "#FACC15"
    noteTextColor: "#713F12"
    loopTextColor: "#E11D48"
---
```mermaid

sequenceDiagram
autonumber
actor Client
participant Auth as Auth Service
participant Gateway as API Gateway
participant Downstream as Station Service

    %% --- CREATION PHASE ---
    rect rgb(238, 242, 255)
        Note over Client, Auth: Phase 1: Token Creation
        Client->>Auth: POST /api/v1/auth/login
        Note over Auth: 1. Base64URL encode Header & Payload<br/>2. Compute Signature = HMAC-SHA256(Header.Payload, Secret)<br/>3. Token = Header.Payload.Signature
        Auth-->>Client: Return JWT
    end

    %% --- VERIFICATION PHASE ---
    rect rgb(248, 250, 252)
        Note over Client, Downstream: Phase 2: Gateway Verification (Zero-I/O)
        Client->>Gateway: GET /api/v1/stations/101<br/>[Header: Bearer JWT]
        
        Note over Gateway: 1. Split Token -> Header, Payload, Actual Signature<br/>2. Compute Expected Signature = HMAC-SHA256(Header.Payload, Secret)<br/>3. Check Expected Signature == Actual Signature
        
        %% Wrapped inside a styling rect to frame/border the alt block prominently
        rect rgb(255, 255, 255)
            alt Signature Mismatch OR Expired (Current Time > exp)
                Gateway-->>Client: 401 Unauthorized
            else Valid Signature & Token Active
                Note over Gateway: 4. Extract 'sub' claim (userId)<br/>5. Inject 'X-User-Id' into request header
                Gateway->>Downstream: GET /api/v1/stations/101<br/>[Header: X-User-Id = user_1042]
                Downstream-->>Client: 200 OK (Response Data)
            end
        end
    end
```mermaid
flowchart TD
subgraph PHASE1 ["Phase 1: Startup & Bean Creation (Runs Once at Boot)"]
direction TB
A["Spring Boot Application Starts"] --> B["Component Scan detects @Component<br/>JwtAuthGatewayFilterFactory"]
B --> C["Spring instantiates Factory as a Singleton Bean"]
C --> D["Gateway parses application.yml<br/>Finds filter rule: '- JwtAuth'"]
D --> E["NameUtils appends 'GatewayFilterFactory'<br/>Matches name to JwtAuthGatewayFilterFactory Bean"]
E --> F["Gateway calls factory.apply(config) ONCE"]
F --> G["apply() returns pre-built GatewayFilter lambda:<br/>(exchange, chain) -> Mono<Void>"]
G --> H["Filter instance attached to Route in memory<br/>(e.g., station-service route)"]
end

    subgraph PHASE2 ["Phase 2: Runtime Execution (Runs Per Request)"]
        direction TB
        I["Client Request Hits Gateway<br/>GET /api/v1/stations/101"] --> J["RoutePredicateHandlerMapping<br/>Matches URL to Station Service Route"]
        J --> K["Gateway retrieves pre-built GatewayFilter<br/>from Route definition"]
        K --> L["Execute GatewayFilter Lambda"]
        
        L --> M{"Authorization Header<br/>Present & 'Bearer '?"}
        M -- No --> N["Set HTTP 401 Unauthorized<br/>Return exchange.getResponse().setComplete()"]
        
        M -- Yes --> O{"Verify Signature (HMAC-SHA256)<br/>& Check 'exp' Claim"}
        O -- Invalid / Expired --> N
        
        O -- Valid --> P["Extract 'sub' claim (userId)"]
        P --> Q["Mutate Request:<br/>Inject 'userID' Header"]
        Q --> R["Call chain.filter(mutatedExchange)"]
        R --> S["Proxy Request to Downstream Microservice"]
    end

    PHASE1 ==>|Route Configured in Memory| PHASE2
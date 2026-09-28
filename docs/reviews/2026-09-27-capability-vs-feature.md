# Capability vs feature: onde deve morar o domínio compartilhado

Data: 2026-09-27

Escopo: aprofundar a decisão ADR-004 do [estudo de modularização](2026-09-27-modularization-study.md). A proposta era tirar o domínio `deeplink/folder` de `feature:deeplink` e criar um tier próprio, chamado lá de `:capability:deeplink:{api,impl,ui,testing}`.

Este documento responde:
- quem usa essa estratégia;
- por que ela ajuda;
- de onde vem o nome;
- por que não manter tudo no `api/impl` da própria feature.

Também corrige pontos do estudo anterior que a pesquisa não sustentou.

Como foi feito:
- **Pesquisas.** Três pesquisas independentes, cada uma revisada por um verificador adversarial:
  - quem separa domínio e telas, e com que nome;
  - origem do termo;
  - argumentos a favor e contra.
- **Análise do código.** Uma análise do repo comparando três alternativas em sete cenários de mudança.
- **Advogado do diabo.** Um agente defendendo manter tudo na feature.
- **Fontes.** 93 achados novos com URL, em [2026-09-27-modularization-sources.md](2026-09-27-modularization-sources.md) (Parte 2, IDs `CAP-W`, `CAP-N` e `CAP-Y`).

> **Limite da pesquisa.** Neste passe a cota de busca web estava esgotada. As fontes foram re-buscadas diretamente por URL (curl, `gh api`, Wayback), então pode faltar alguma fonte que só uma busca encontraria. Isso importa principalmente para a afirmação "nenhum app grande usa o nome capability" (§5), que é ausência de evidência, não prova.

---

## 0. Resumo

**A decisão:** o domínio que várias telas usam fica **dentro de uma feature de tela** ou **num tier próprio, sem telas**?

Conclusões:

1. **Separar domínio compartilhado das telas é prática mainstream, mas quase ninguém chama isso de "capability".**
   - Slack e Airbnb: *services*.
   - Google: *data modules*.
   - Now in Android: `core:data`.
   - Bitkey (Block, KMP) e Wire Kalium (KMP): `:domain:*`.
   - Tivi: `:data:*`.
   - Element X: `libraries:matrix`.

   Detalhes em §2.1.
2. **O contra-campo é legítimo e roda em produção.**
   - Thunderbird documenta que "business objects should live in their respective feature modules".
   - DuckDuckGo usa `-api/-impl/-store` por feature, com as regras verificadas no build.
   - Element X expõe stores e use cases no `:api` de features.

   Detalhes em §2.2.
3. **A diferença entre os dois campos é mais de vocabulário do que de estrutura.**
   - Onde "feature" significa **área de domínio** (Thunderbird, DuckDuckGo), as telas são sub-partes e o domínio mora na feature.
   - Neste repo, `home`, `settings` e `data-transfer` são **telas**, e `deeplink` é **domínio + telas** sob o mesmo prefixo.
   - O tier novo existe para desfazer essa ambiguidade. A estrutura segue daí.
4. **"Manter no api/impl da feature" feito direito é quase igual ao tier, do ponto de vista de quem consome.** Na análise do repo, s1 e s5 dão resultado idêntico, e em s2 nenhum arranjo reduz o alcance da mudança. O tier só vence em quatro coisas concretas (§4.2, §3.5):
   - consumidores sem UI (widget, extensão iOS, histórico);
   - dono único do schema;
   - regras de dependência que o build consegue verificar;
   - testar o domínio sem compilar Compose.
5. **O nome "capability" foi escolha do estudo anterior e tem pouca base.**
   - Na origem (Lewis & Fowler, 2014), uma business capability é uma fatia vertical **com UI**. O tier aqui é **mais estreito**: sem telas e sem navegação.
   - Nenhuma empresa grande usa o termo como tipo de módulo. O único precedente open-source de porte médio é o Duck Detector, com domínio não-CRUD.
   - Recomendação: renomear para **`domain`**, a linhagem do Bitkey (Block, KMP), que tem regras de tier escritas (§5.4).
6. **Correções ao estudo anterior** (§6):
   - schema por domínio não é consenso em KMP;
   - o critério "api/impl" não é o critério certo para decidir um tier;
   - são 35 arquivos, não 33;
   - os 6 use cases sem actual por plataforma deixam de ser interfaces próprias: viram comandos do repositório, um port pequeno ou uma classe concreta onde está o consumidor.

---

## 1. O problema que a decisão resolve

`feature:deeplink:api` tem **35 tipos**: 33 em commonMain e 2 em jvmMain. Cada consumidor usa uma fatia diferente deles (CAP-Y-18):

| Consumidor | O que usa de `deeplink:api` |
|---|---|
| `settings:impl` | só `domain.repository` e `domain.usecase` (`FolderRepository`, `DeleteAllDeepLinks`, `LaunchDeepLink`) |
| `data-transfer:impl` | só modelos, os 2 repositórios e `ValidateDeepLink` |
| `shared` | só a rota (`DeepLinkRouteEntryPoint`) |
| `deeplink:ui-component` | modelos, `ui.formatting`, `ui.model` |
| `home:impl` | quase tudo: domínio, UI models, rota, enriquecimento, alvo JVM |

Do inventário completo (análise do repo, §7.1):
- **17 dos 35** tipos têm consumidor fora de `feature:deeplink`.
- **17** são usados só pela própria feature.
- **1** está morto (`AnalyticsUserProperties`, 0 referências).
- Só **1** é contrato de navegação.

Isso viola dois princípios de pacote de Robert C. Martin, que tratam um "package" como unidade binária, ou seja, um módulo Gradle hoje (CAP-Y-17):
- **CRP (Common Reuse):** "classes that are used together are packaged together". `settings:impl` e `data-transfer:impl` dependem de um módulo cujos UI models e rotas nunca usam.
- **CCP (Common Closure):** "classes that change together are packaged together". O schema SQLDelight de deeplink/folder está em `core:database`, separado dos repositórios que mudam junto com ele (CAP-Y-23).

**Na prática:** hoje, adicionar um campo a `DeepLinkListItem` dispara a recompilação dos **6** consumidores de `deeplink:api`. Com o conteúdo separado, seriam **2** (cenário s1, §4.2).

---

## 2. Quem faz o quê: as duas escolas

### 2.1 Escola A: um tier próprio, sem UI, para domínio e dados compartilhados

| Projeto | Nome do tier | Estrutura real | Regra escrita | Persistência | IDs |
|---|---|---|---|---|---|
| **Slack** (iOS + Android, 2022) | *Services* | Features / Services / Libraries; no iOS, services em Interface/Implementation | "Services generally do not contain UI code"; no Android, "business logic that spans one or more features" | Services cuidam de "persistence" | CAP-W-01, CAP-N-14, CAP-Y-07 |
| **Airbnb iOS** (2021) | *service* | 12 tipos de módulo; o post nomeia só feature, feature interface e service | "non-UI objects ... managing state that is shared between features"; feature não depende de feature | — | CAP-W-02, CAP-Y-08 |
| **Afterpay** (blog da Cash App, 2021) | *Service* | Feature / Service / Core, com `:api`, `:impl`, `:wiring`, `:demo`; `:fakes` só em services | "Lower-level modules never depend on higher-level modules". O próprio autor diz que o desenho não foi validado em escala | — | CAP-W-03 |
| **Google** (guia oficial, 2026) | *data module* | Um módulo por domínio, que expõe o repositório e esconde os data sources | "Encapsulate all data and business logic of a certain domain"; "Feature modules depend on data modules"; features trocam **IDs** e carregam os dados do data module | No data module | CAP-W-04, CAP-Y-01, CAP-Y-02, CAP-N-24 |
| **Now in Android** | `core:data`, `core:domain`, `core:model` (horizontal, não por domínio) | `feature:*:api` contém **só** a NavKey | "If a class is needed only by one feature module, it should remain within that module. If not, it should be placed into an appropriate `core` module." | `core:database` | CAP-W-05, CAP-Y-05 |
| **Bitkey** (Block, KMP) | `:domain:<x>` | 32 unidades `:domain:*` (quase todas `public/impl/fake`), mais os tiers `:libs:*` e `:ui:*` | README: "must remain independent of UI"; domain pode depender de domain | **Central**: os 70 `.sq` ficam em `:domain:database:public` | CAP-W-07 |
| **Wire** (KMP) | `domain:*` em SDK separado | Toda a lógica e persistência ficam no repo **Kalium** (`core → data → domain → logic`), consumido pelo app como included build | "Dependency direction is intentionally one-way" | **Central**: `:data:persistence` | CAP-W-08 |
| **Tivi** (KMP, arquivado) | `:data:<domain>` | 19 módulos `:data:*`, separados das telas `:ui:*` | — | **Central**: `:data:db-sqldelight` | CAP-W-06 |
| **Element X** (Android) | `libraries/*`, `services/*` | `libraries:matrix:{api,impl,test}` (wrapper do SDK) fica fora das features | "`features` modules contain some UI"; "`libraries` modules contain classes that can be useful for other modules" | Por unidade: 3 libraries com banco próprio | CAP-W-09, CAP-N-17 |
| **Mozilla** | *components* do application-services (Rust com bindings Kotlin/Swift) | Unidades de lógica compartilhada sem UI usadas pelos apps Firefox. O android-components (`concept-*`/`browser-*`/`feature-*`) é outra coisa: um split api/impl com outros nomes | "components should always depend on concept modules (not their implementations)" | — | CAP-W-10, CAP-N-19 |
| **isowords** (Point-Free, iOS) | `*Client` | `*Feature` (UI) e `*Client` (interfaces + `*Live`) | — | `LocalDatabaseClient` | CAP-W-22 |
| **Duck Detector** (Android, 1.052 estrelas) | **`:capability:<unit>`**, literal | `:app → :sdk → :feature → :capability → :core` | "A capability exists only because at least two features consume the same evidence"; capability não depende de capability; regras verificadas pelo build | — | CAP-W-11 |

Nota: o **Shopify** (backend Rails, 2019) organiza *components* por domínio de negócio, com API pública e "exclusive ownership of its associated data" (CAP-W-23). Mas cada componente é um "mini rails app" **vertical**, com UI (CAP-N-20). Ele serve de precedente para "unidade dona dos dados", não para "unidade sem UI".

### 2.2 Escola B: o domínio mora na feature (ou não existe tier nenhum)

| Projeto | Como faz | Evidência | IDs |
|---|---|---|---|
| **Thunderbird Android** | "Business objects (e.g., account, mail, etc.) should live in their respective feature modules." Uma feature é uma **área de domínio** (account, mail); telas e partes sem tela são sub-features. O ADR-0009 renomeia `impl` para `internal`, e o `:api` carrega "repositories, use cases, service interfaces" + contratos de navegação. O código ainda não acompanha totalmente o documento | `module-organization.md`, ADR-0009 | CAP-W-12 |
| **DuckDuckGo Android** | 71 diretórios com `-api/-impl` (+`-store` para persistência, `-internal`). "Only `:app` depends on `-impl` modules", verificado no build. `saved-sites-api` mistura repositórios com hooks de UI e é consumido por 5 módulos | `.claude/docs/architecture.md` | CAP-W-13 |
| **Element X** (domínio de produto) | O `:api` das features exporta stores e use cases: `SeenInvitesStore` fica em `features/invite/api`, com DataStore em `invite/impl`, e é consumido por **4 outras features** (home, joinroom, space, preferences). O fake fica em `features/invite/test`. Features também exportam Compose (`PollContentView`). Escala: 45 features com `:api` e 120 módulos sob `features/` (árvore `develop` em 2026-09-27) | Árvore `develop` | CAP-Y-09 (poll); advogado do diabo §A1 (`SeenInvitesStore`) |
| **Proton Mail** | Unidades verticais `:mail-*:{dagger,data,domain,presentation}`. Unidades de entidade (`mail-message`) e de tela (`mail-mailbox`) ficam lado a lado, e telas dependem direto do domain **e** da presentation de outras unidades | `settings.gradle.kts` | CAP-W-14 |
| **Square** (7.000+ módulos) | Um único formato para tudo, sem nome de tier: `:public/:impl-real/:impl-fake` em 2026. Telas usam o mesmo formato | Deck de 2019 (telas: `:account-screen:public`, `:fake`, `:demo`); Block, 2026 (escala e formato) | CAP-W-21 |
| **App Platform** | Toda "library" tem `:public/:impl/:internal/:testing/:robots`; `:public` pode conter UI. A UI se separa por Presenter/Renderer, não por tier | `module-structure.md` | CAP-W-15, CAP-Y-11 |
| **Tuist TMA** (iOS) | "A module represents an application feature", com cinco targets: Source, Interface, Tests, Testing e Example | Docs do Tuist | CAP-W-31 |

### 2.3 O que separa as escolas

A síntese do verificador (CAP-W) resume assim: **"What separates A from B is vocabulary more than structure."**

- Na escola B, "feature" já quer dizer "área de domínio". Thunderbird tem `feature:account`, e dentro dela ficam telas e repositórios. A escola A usa "feature" para "tela ou fluxo" e precisa de outra palavra para o domínio.
- As duas concordam em quase tudo o resto:
  - `impl → impl` é proibido;
  - quem consome depende de contrato;
  - existem fakes por unidade;
  - o app é o único que vê as impls.
- Nenhuma das duas **documenta** que o contrato de domínio deve carregar modelos de lista, formatação e rotas para consumidores que não precisam deles. Na prática há exceções:
  - o `saved-sites-api` da DuckDuckGo tem o mesmo formato do `deeplink:api` atual;
  - o `poll/api` do Element X exporta Compose junto com actions (CAP-Y-09);
  - o `:public` do App Platform pode conter UI (CAP-W-15);
  - o `account:common` do Thunderbird mistura entidades, repositório e Compose (CAP-W-12).

  Considerar isso anti-exemplo é **opinião deste documento**, baseada no CRP (§1).

**O caso deste repo.** `feature:home`, `feature:settings` e `feature:data-transfer` são telas; `feature:deeplink` é domínio + telas. O mesmo prefixo cobre dois conceitos. Qualquer regra escrita sobre "feature" ou vai errar para um lado, ou precisa de exceção.

### 2.4 Apps pequenos não separam

Wikipedia Android (`:app` + 1) e o app da KotlinConf (lógica em `:app:shared`) não separam nada (CAP-W-19). A separação aparece de "dezenas de módulos" para cima.

Relacionado, mas diferente: o NiA diz "If your data layer is small, it's fine to keep it in a single module" (CAP-N-12). A frase é sobre **não dividir** `core:data` em vários módulos, não sobre deixar dados dentro de features.

---

## 3. Por que separar ajuda

Em §3 e §4, "capability", "tier" e "C" se referem ao tier como o estudo anterior o propôs. O nome recomendado está em §5.4, e os papéis corrigidos em §6.1.

Os argumentos abaixo vêm na ordem de força da evidência. Cada um diz também o que **não** resolve.

### 3.1 Cada consumidor depende só do que usa (CRP)

`settings:impl` e `data-transfer:impl` passam a depender de um contrato só de domínio. Mudanças em modelos de lista, formatação ou rotas deixam de atingi-los (CAP-Y-17, CAP-Y-18, CAP-Y-19).

**Não resolve** mudanças no próprio contrato. Trocar a assinatura de `DeepLinkRepository` atinge os mesmos consumidores em qualquer arranjo (cenário s2, §4.2).

### 3.2 A regra oficial do Google: features dependem de data modules e trocam IDs

> "Instead of passing objects, modules should exchange primitive IDs and load the resources from a shared data module." (CAP-N-24, CAP-Y-02)

A página não diz que o dado pertence à tela que o cria. Diz que o dado que várias telas leem fica num módulo de domínio que as telas consomem (CAP-Y-01).

### 3.3 Consumidores sem tela

É o argumento mais forte para o futuro. Exemplos:

- **NiA `:sync:work` (WorkManager).** Depende de `core:data` e de nenhum módulo de feature. Isso só funciona porque nenhuma tela é dona dos dados (CAP-Y-06).
- **Glance widgets.** Não se misturam com Compose de app ("Avoid mixing the two"). App shortcuts são publicados em runtime pelo código do app (CAP-Y-21). Este repo já tem um port de shortcut (`DeepLinkShortcutManager`) dentro do `:api` da feature.
- **Extensões iOS e App Intents.** Compartilham código por framework, e esse framework precisa ser extension-safe. Um framework KMP inclui todas as suas dependências (CAP-Y-22). Uma share extension que lista deeplinks precisaria de um umbrella **só de domínio**, sem features nem Compose, o que exige o domínio num módulo separado das telas. Essa conclusão é inferência deste documento: nenhuma fonte a afirma diretamente.
- **SwiftUI nativo.** A JetBrains separa `sharedLogic` (sem Compose) de `sharedUI` quando alguma plataforma usa UI nativa. Num app 100% CMP, como este hoje, o default dela é um módulo só (CAP-Y-12). O argumento vale como **gatilho**, não como recomendação atual.

### 3.4 Um dono para dados, schema e invariantes

- **Hoje escrevem dados de deeplink/folder quatro módulos:** `deeplink:impl`, `home:impl` (`upsertDeepLink` após lançar e ao favoritar), `data-transfer:impl` (import sem transação) e `settings:impl` (`folderRepository.deleteAll()` direto). As invariantes (URL única, pasta, desativação de shortcuts) estão espalhadas. Da revisão de 19/09, o F3 (import não atômico) é cruzado entre módulos, e o F1 e o F2 vêm em parte do upsert de entidade inteira que todos esses escritores usam.
- **Evans pede fronteira física.** O bounded context deve aparecer em "code bases and database schemas" (CAP-N-25). O SQLDelight guarda as migrações ao lado dos `.sq` e as verifica por projeto (CAP-Y-23).
- **Capability com comandos por ID reduz os escritores de entidade inteira a um.** Uma capability que expõe só comandos por ID (`rename(id, name)`, `moveToFolder(id, folderId)`) faz isso. **Não resolve** sozinha: um modelo anêmico continua anêmico (§3.8).

### 3.5 Regras que o build consegue verificar

A diferença mais subestimada. Com o tier:
- `home:impl → capability:deeplink:api` é uma aresta **feature → capability**, permitida;
- `home:impl → feature:deeplink:*` continua **feature → feature**, proibida exceto para os papéis `api` (keys) e `ui` (widgets embutíveis, §6.1).

Sem o tier, permitir que `home` use o domínio de `deeplink` exige abrir exceção por sufixo, por exemplo `feature:*:model`. Isso é **inventar o tier com outro nome**, só que escondido dentro da regra (análise do repo, B vs C, ponto 3). O mesmo vale para "api de capability não pode ter Compose nem navegação": só é expressável se o módulo tiver um tipo próprio.

### 3.6 Testar domínio sem compilar UI

Hoje `deeplink:impl` aplica o plugin Compose. Um teste de contrato do repositório em `commonTest`, rodando em JVM e iOS com driver em memória, compila também os 31 arquivos de UI e as dependências Compose **para cada alvo**. Numa capability sem Compose, o teste compila só domínio e SQL.

Esse custo é **medível** (compare `iosSimulatorArm64Test` dos dois módulos), mas ainda **não foi medido** (advogado do diabo, gatilho 4).

### 3.7 Build: real, mas menor do que parece, e mais fraco no iOS

- **Gradle:** uma mudança de ABI dispara as tasks de compilação de todos os consumidores diretos (CAP-Y-19).
- **Kotlin/JVM:** o incremental por classpath snapshot recompila só as classes afetadas. O custo real é "tasks re-executadas + recompilação parcial", não "tudo recompila" (CAP-Y-20). Funções `inline` públicas entram na ABI.
- **Kotlin/Native:** incremental de klib é Beta, opt-in e **não está ligado neste repo** (CAP-Y-33). Nenhuma fonte quantifica o custo no iOS.

**Conclusão:** o argumento de build é secundário neste porte. Não justifique o tier por ele sem um build scan.

### 3.8 O que o tier não resolve (e pode piorar)

| Risco | Descrição | Fonte |
|---|---|---|
| **Sticky capability só renomeada** | Dehghani: a "sticky capability" é um conceito vazado do qual todo mundo depende. O remédio é **desconstruí-la** em conceitos de domínio bem definidos. Ela desaconselha extraí-la como está: "it will just result in a similar tight coupling". Renomear `feature:deeplink:api` para `:capability:deeplink:api` sem tirar UI models, rotas, formatação e tipos JVM repete o anti-padrão | CAP-N-07 |
| **God capability** | "if your modules are growing too large you might end up with yet another monolith" (Google); "Keep this kernel small" (Evans) | CAP-Y-28 |
| **Modelo anêmico** | `DeepLink`/`Folder` como sacos de campos + 14 interfaces de use case de um método é o *Anemic Domain Model* de Fowler. Mudar de módulo não muda isso | CAP-Y-29 |
| **Ciclos entre capabilities** | Com várias capabilities, uma acaba precisando da outra. Não há consenso: Duck Detector proíbe capability → capability, Bitkey permite domain → domain, Afterpay diz "generally" | CAP-Y-30, CAP-W síntese |
| **Abstração prematura** | Interface por classe tem custo de manutenção sem ganho. Fowler: "Using interfaces when you aren't going to have multiple implementations is extra effort" | CAP-Y-27 |
| **Ports públicos à toa** | No tier, ports usados só pelas telas de deeplink (`ShareDeepLink`, handlers) precisam ficar públicos, porque a impl está em outro módulo. Com tudo na feature, ficariam `internal` | análise do repo §1 |

---

## 4. "Por que não manter tudo no api/impl da própria feature?"

### 4.1 Resposta curta: pode, e às vezes deve

O Element X faz exatamente isso para domínio de produto, e roda em produção com 45 features e 120 módulos sob `features/`:
- `SeenInvitesStore` fica no `:api` de `features/invite`;
- a implementação com DataStore fica em `invite/impl`;
- 4 outras features o consomem;
- o fake fica em `features/invite/test`.

Thunderbird documenta como regra. DuckDuckGo verifica no build.

**Manter na feature funciona quando:**
- o domínio é consumido principalmente pelas telas da própria feature, e outras features usam só keys ou uma fatia pequena e estável via um `:api` enxuto (como `SeenInvitesStore`);
- há uma linguagem só e um time só (DDD: bounded context acompanha mudança de linguagem, CAP-N-05);
- nenhum consumidor precisa do domínio sem as telas;
- o `:api` segue uma disciplina rígida: só o que outros usam, sem UI models, com ABI travada.

O problema deste repo **não é** "api/impl dentro da feature". É o `:api` carregar três contratos.

### 4.2 Comparação no código: A, B e C

A análise do repo comparou três arranjos:

- **A: hoje.** `feature:deeplink:{api,impl}` gordo.
- **B feito direito:** fica dentro da feature, com:
  - `:api` só com rotas;
  - um `:model` público com domínio, repositórios e ports compartilhados;
  - modelos de lista e formatação em `ui-component`;
  - `:impl` com telas + repositórios + actuals.
- **C: tier.** `:capability:deeplink:{api,impl,ui,testing}` + `:feature:deeplink:{api,impl}`.

| Cenário | A | B feito direito | C |
|---|---|---|---|
| **s1** Campo novo em `DeepLinkListItem` | recompila 6 (inclui settings e data-transfer, que não usam o tipo) | recompila 2 | recompila 2 |
| **s2** Mudar assinatura de `DeepLinkRepository` (comandos por ID) | recompila 6 | recompila 5 | recompila 7 (+fakes). **Nenhum arranjo reduz isso**; C ganha um contract test que fake e real precisam passar |
| **s3** Widget / share extension iOS / App Shortcut que lista e lança sem navegação | puxa rotas, `core:navigation` (compose-navigation) e as telas; no iOS, só existe o framework inteiro | tipos limpos, mas as impls continuam junto das telas: **mesmo problema** | depende só de `cap.api + cap.impl + core:sqlite`; um umbrella iOS só de domínio é possível |
| **s4** Um time novo assume folders | só CODEOWNERS espalhado por 5+ módulos | telas de folder vão para `:feature:folder`; o domínio fica em `:feature:deeplink:model`, de outro time | igual a B nas telas; folders **ficam** na mesma capability (FK, SQL cruzado, mesma transação) |
| **s5** Tela SwiftUI nativa reusando o domínio | o export leva rotas, `ImmutableList` e formatação para o header Swift | exporta `:model`, só domínio | exporta `cap.api`, só domínio. **Equivalente a B** |
| **s6** Feature nova "histórico" | depende dos 35 arquivos; quem escreve o histórico é `F.impl` | `history:impl → feature:deeplink:model` é aresta feature → feature; quem escreve ainda está junto das telas | `history:impl → cap.api`; tabela de eventos e `recordLaunch` ficam em `cap.impl`; `feature:deeplink` não muda |
| **s7** Migração de schema | edita `core/database` + `F.impl` (2 módulos para uma mudança de dado) | igual a A, a menos que o schema também mude de módulo | tudo em `cap.impl`: `.sq`, `.sqm`, mapper, repositório, teste de migração |

**Leitura honesta:**
- Para quem **consome** (home, settings, data-transfer), B feito direito expõe o mesmo que C. s1 e s5 dão igual.
- C vence em s3, s6 e s7, e na expressividade das regras (§3.5).
- B vence em número de módulos e em manter `internal` os ports usados só pelas telas de deeplink.
- **Se B também separar a impl em "telas" e "dados", vira C com outro path.** Cerca de 50 dos 99 arquivos de `deeplink/impl` não são UI, então essa metade tem massa própria.

### 4.3 O caso do advogado do diabo e a resposta a cada argumento

| Argumento contra o tier | Força | Resposta |
|---|---|---|
| **A1.** Element X põe domínio de produto no `:api` da feature, em produção | Forte | Verdade, e `SeenInvitesStore` tem 4 consumidores externos, ou seja, o Element X aceita feature → feature api para domínio. Isso prova que a escola B funciona em escala. A diferença de grau: `SeenInvitesStore` é um detalhe da feature de convites, enquanto `deeplink` é o domínio central, escrito por 4 módulos e lido por quase todos |
| **A2.** App Platform não separa feature de domínio | Médio | Correto. Ele separa UI por Presenter/Renderer e usa formato uniforme; no sample, `UserManagerImpl` convive com presenters e renderers no mesmo `:impl` (CAP-W-15). É a escola B, válida. O que ele exige é que consumidores vejam só `:public`, o que a poda de §7.2 também entrega |
| **A3.** Fowler/YAGNI: abstração antecipada tem custo de carregamento | Forte para interfaces | Aceito para interfaces: os 6 use cases sem actual deixam de ser interfaces 1:1 e viram comandos, um port ou uma classe concreta (§6.4). O YAGNI de Fowler exclui "effort to make the software easier to modify" (CAP-Y-27) |
| **A4.** Fowler: "split your top level into domain oriented modules which are internally layered" | **O mais forte** | É exatamente o que B faz. A resposta é a mesma de §2.3: aqui o topo `feature/` mistura unidades de tela e de domínio. Se o topo fosse orientado a domínio (`deeplink/`, `settings/`), B estaria certo. O tier é uma forma de chegar a "top level orientado a domínio" sem renomear as telas |
| **A5.** DDD: há um contexto e uma linguagem; chamar de capability não cria fronteira nova | Correto | O tier não cria contexto novo: ele dá ao contexto único um lugar **fora das telas**. A fronteira que ele materializa é "telas × dados que várias telas usam", não "contexto × contexto" |
| **A6.** Conway: um autor, nenhuma estrutura para copiar | Correto para o repo | Não se aplica ao objetivo de laboratório: o exercício é preparar fronteiras para times futuros (CAP-N-10, CAP-Y-25) |
| **A7.** Google e NiA alertam contra modularizar demais | Correto | O NiA, mesmo sendo vitrine, separa dados das telas (`core:data`). O alerta é sobre granularidade, não sobre essa separação |
| **A8.** Slack não impõe pares api/impl por padrão; usa pares para desfazer ciclos, e aqui não há ciclo | Correto | Concordo sobre pares. Mas o Slack separa *services* de *features* mesmo sem impor pares. A decisão de tier (§6.3) é independente da decisão de par |
| **A9.** A ABI do domínio é instável (F1–F3, F6): extrair agora paga a migração duas vezes | **Forte, e muda a ordem** | Aceito. O roteiro já põe as correções (fase 1) antes da extração (fase 6). E a poda do `:api` (§7.2) é a **primeira metade** da extração, não uma alternativa a ela |
| **A10.** O problema é conteúdo do `:api`, não camada ausente | Parcialmente certo | A poda resolve s1 e s5, e resolve s7 se o schema também for para `feature:deeplink:impl` (advogado do diabo §8.8). Não resolve s3, s6 nem as regras de §3.5 |
| **A11.** Custo de carregar: mais `Kind`s, duas árvores para o mesmo substantivo, a pergunta "é feature ou capability?" | Real | A pergunta é o objetivo: é ela que força decidir quem é dono do dado. O custo de regras cai se os papéis forem enxutos (§6.1) |

### 4.4 Gatilhos: quando a feature basta e quando extrair

**A feature basta (escola B) quando TODAS valem:**
- o domínio é consumido principalmente pelas telas da própria feature, e outras features usam só keys ou uma fatia pequena e estável via `:api` enxuto;
- nenhum consumidor sem UI existe nem está planejado;
- um time, uma linguagem;
- o `:api` passa pela poda: sem UI models, sem formatação, ABI travada.

**Extraia um tier de domínio quando QUALQUER uma vale:**
1. Duas ou mais features de tela leem ou escrevem os mesmos dados. É a regra do NiA ("If not, it should be placed into an appropriate `core` module") e do Duck Detector ("at least two features").
   - **Neste repo, isso já é verdade**: home, settings e data-transfer, e com escritas, não só leituras.
   - Este gatilho é **convenção, não lei**. O Element X o viola de propósito (`SeenInvitesStore`, com 4 consumidores), e o advogado do diabo o contesta.
   - O que pesa aqui é que `deeplink` é o domínio central de todo o app, não um detalhe de uma feature.
2. Um consumidor precisa do domínio sem telas: widget, shortcut handler, WorkManager, extensão iOS, CLI desktop, SwiftUI nativo.
3. Um segundo time assume os dados ou as telas.
4. Testar o domínio passa a compilar Compose e isso aparece medido.
5. Um build scan mostra recompilação relevante por mudanças que o consumidor não usa.
6. Surge um segundo app ou white label com UI diferente sobre o mesmo domínio.

---

## 5. O nome "capability"

### 5.1 De onde veio

O nome foi **escolha do estudo anterior**. Ele mapeou os "Services" do Slack para "capability" e o introduziu como tipo de módulo. Nenhuma empresa grande documenta o termo assim (CAP-Y-13). O único precedente open-source de porte médio é o **Duck Detector** (1.052 estrelas, CAP-W-11). Ele tem a mesma regra ("at least two features") e um domínio não-CRUD (evidências de integridade do dispositivo). Os outros são dois repos pessoais (CAP-Y-35).

### 5.2 A linhagem do termo

| Onde | O que "capability" significa | IDs |
|---|---|---|
| **Business architecture** (TOGAF G211/G233; o texto oficial está atrás de login e não foi lido) | Na formulação de Richardson: "something that a business does in order to generate value", relativamente estável | CAP-N-13, CAP-N-03 |
| **Lewis & Fowler, *Microservices* (2014)** | Serviços "organized around business capabilities" são "**broad-stack** ... **including user-interface**, persistant storage". Uma capability, na origem, é uma fatia **vertical com UI**, o que este repo chama de feature | CAP-N-01, CAP-Y-15 |
| **Richardson, microservices.io** | "Decompose by business capability" e "Decompose by subdomain" são alternativas com o texto de forças **byte-idêntico**: dois vocabulários para o mesmo objetivo | CAP-N-03, CAP-N-04 |
| **Dehghani (martinfowler.com, 2018)** | "sticky capability": o conceito vazado que todos usam (§3.8) | CAP-N-07 |
| **Times (Narayan; Team Topologies)** | Time alinhado a uma área de negócio de longo prazo. Serviços transversais (storage, rede) ficam com o *platform team* | CAP-N-08, CAP-N-09 |
| **DDD (Evans)** | **Não é termo de DDD.** Aparece duas vezes no DDD Reference, ambas incidentais. O equivalente DDD é *bounded context* / *subdomain* | CAP-N-25, CAP-N-05 |
| **Google, guia de modularização** | "Diverse capabilities" = várias implementações de uma API (OpenGL/Vulkan). **Não** dá suporte ao nome | CAP-N-11, CAP-Y-03 |

**Conclusão.** O tier proposto é mais estreito que o sentido original: é o contrato e a implementação de um bounded context, **sem telas nem navegação**. Quem conhece o termo por microservices vai lê-lo como vertical com UI, e quem pesquisa "capability module Android" não encontra referência canônica.

### 5.3 Colisões

- **Gradle:** uma capability de componente é "identified by a (group, module, version) triplet" na resolução de dependências (CAP-W-28, CAP-N-22).
- **Xcode:** Capabilities (Push, iCloud, In-App Purchase).
- **Android App Actions:** elemento `<capability>` em `shortcuts.xml`. Relevante para um launcher que gerencia shortcuts (CAP-N-26).
- **Este repo:** `iosApp/deeplinklauncher/Info.plist` já tem `UIRequiredDeviceCapabilities` (CAP-N-26).
- **PIA VPN**, o único app de produção encontrado com um tier `:capabilities`, usa o termo para **concerns transversais** (UI compartilhada, analytics, flags), não para domínio (CAP-Y-16).

Nenhuma dessas colisões quebra o build. O custo é ruído em buscas e ambiguidade para quem lê.

### 5.4 Alternativas e recomendação

| Nome | Precedente | Problema |
|---|---|---|
| `service` | **O mais forte na indústria**: Slack, Airbnb, Afterpay | Colide com `android.app.Service`; Fowler chama o termo de polissemia |
| `domain` | **KMP enterprise**: Bitkey (`:domain:<x>:{public,impl,fake}`, "must remain independent of UI", domínio + dados). Wire Kalium também usa `:domain:*`, mas lá a persistência fica em `:data:persistence` | Colide com o "domain layer" (opcional) do Google, com o `:core:domain` do NiA (só use cases) e com o domain da Clean Architecture, que exclui dados |
| `data` | Google ("data module"), Tivi | Sugere só persistência, embora o Google inclua "business logic" |
| `library` / `lib` | Slack, Element X, Signal | Sugere código genérico; costuma misturar UI (Element X, Signal) |
| `core` | NiA | Google: core "don't represent any specific layer"; tende a virar depósito de tudo (opinião, sem fonte primária) |
| `kit` | Grab | Na Grab é a **ponte** entre features, não o domínio |
| `component` | Shopify, Mozilla | Colide com os *app components* do Android; na Shopify é vertical |
| `platform` | — | Colide com source sets de plataforma do KMP e com o *platform team* |
| `capability` | Duck Detector (porte médio), PIA (outro sentido), 2 repos pessoais | Sentido de origem mais amplo (vertical, com UI); colisões de §5.3; sem linhagem em empresa grande |

**Recomendação: `domain`.**

- **Linhagem KMP.** O objetivo deste repo é estudar padrões de KMP enterprise. O Bitkey (Block) é um codebase KMP público de porte com regras de tier **escritas**, e usa `:domain:*` exatamente para domínio + dados sem UI. O Wire Kalium usa o mesmo nome, com a persistência num tier `data` à parte. Um nome que você encontra em código real facilita comparar e aprender.
- **Sem colisão de plataforma.** Não colide com nenhum tipo de Android, iOS ou Gradle.
- **A colisão com "domain layer" se contorna com o path.** `:domain:deeplink:api` é um módulo, enquanto `domain/` dentro de um `impl` é um pacote. A definição no `MODULARIZATION.md` fecha a ambiguidade: *"domain module = contrato + implementação (incluindo dados) de um bounded context, sem telas, sem navegação, sem Compose"*.

**Consequência da troca: widgets compartilhados.** O estudo previa `:capability:deeplink:ui` (cards e mappers usados por 2+ features). `:domain:deeplink:ui` é contraditório. Duas saídas:
- **(a) Papel `ui` da feature dona: `:feature:deeplink:ui`** (renomeando o `ui-component` atual). É a opção recomendada.
  - Regra: pode depender só de `:domain:*:api`, core e design system; nunca de navegação nem de impl.
  - Outras features podem depender do `api` e do `ui` de uma feature, nunca do `impl`.
  - Precedente: o Element X exporta UI embutível no `:api` de features (`PollContentView`, CAP-Y-09), e o Airbnb compartilha tipos leves via "feature interface".
  - Evite `:ui:<x>`: no Bitkey e no Tivi, `ui` quer dizer **telas**, e este repo já tem `core:ui`.
- **(b) Manter `capability`, que é neutro quanto a camada.** `:capability:deeplink:{api,impl,ui,testing}` lê bem em todos os papéis. É a **melhor razão para manter o nome**. Se escolher (b), este documento vira a definição e o `MODULARIZATION.md` precisa carregá-la explicitamente, com a ressalva de que o sentido original é mais amplo.

**A troca é reversível.** Renomear módulos é mecânico (`git mv` + `settings.gradle.kts` + accessors). A decisão importante é a estrutura, não o nome.

---

## 6. Correções ao estudo anterior

Emendas à [2026-09-27-modularization-study.md](2026-09-27-modularization-study.md). Este documento prevalece nos pontos abaixo.

### 6.1 ADR-004: nome e papéis

- **Antes:** `:capability:deeplink:{api,impl,ui,testing}`.
- **Depois:**
  - tier renomeado para `domain`: `:domain:deeplink:{api,impl,testing}`;
  - widgets e mappers compartilhados em `:feature:deeplink:ui` (ex-`ui-component`), com dependência só em `:domain:*:api`, core e design system;
  - ou, alternativamente, manter o nome `capability` com a definição de §5.4.
- **Gramática:**
  - `:domain:<x>:(api|impl(-<variante>)?|internal|testing)`, onde `internal` é código compartilhado entre várias impls da mesma unidade (modelo App Platform), criado só sob demanda;
  - `:feature:<x>:(api|impl|ui|demo)`;
  - regras: `domain:*:api` sem Compose e sem navegação; feature pode depender de `api` e `ui` de outra feature, nunca de `impl`.
  - **enforcement:** o `classify()` do estudo (§6.8) coloca todo `:feature:*` que não é `api` em `FEATURE_IMPL`, então `home:impl → feature:deeplink:ui` falharia como "impl → impl". É preciso um `Kind.FEATURE_UI`: `FEATURE_IMPL → FEATURE_UI` permitido; `FEATURE_UI → {DOMAIN_API, CORE, CORE_UI}` apenas. `CAPABILITY_*` vira `DOMAIN_*`.
- **Domain → domain**, via allowlist revisada:
  - `domain:x:impl → domain:y:api` é permitido;
  - `domain:x:api → domain:y:api` só quando um tipo de `y` aparece na assinatura de `x`;
  - `impl → impl` nunca.

  É o meio-termo entre Bitkey (permite) e Duck Detector (proíbe).

### 6.2 ADR-014: schema

- **Antes:** "Schema SQLDelight por capability, mesmo arquivo de banco", apresentado como consenso.
- **O que a pesquisa mostrou:** **não é consenso em KMP.** Os pares mais próximos centralizam o schema (CAP-W-30, CAP-Y-23):
  - Bitkey: os 70 `.sq` em `:domain:database:public`;
  - Tivi: `:data:db-sqldelight`;
  - Wire Kalium: `:data:persistence`.

  Element X, Shopify e isowords deixam cada unidade dona do seu armazenamento. A DuckDuckGo é mista: cada feature tem seu `-store`, mas as entidades de saved-sites são registradas no `AppDatabase` central.
- **Regra corrigida:**
  - **Um domínio com um banco (este repo hoje):** o schema vai para `:domain:deeplink:impl`, porque é o único dono e CCP manda os `.sq`/`.sqm` ficarem junto dos repositórios. Mantenha o nome do banco (`dll-db`), o pacote e a classe.
  - **Vários domínios com um banco:** schema central num `:domain:database` (modelo Bitkey), com DAOs internos, e cada domínio expõe só repositórios. Dividir o schema em módulos SQLDelight exige o mesmo nome de banco em pacotes diferentes, e as migrações ficam presas a cada projeto (CAP-Y-23). Não vale o custo sem necessidade.
  - **Um banco por domínio:** só quando os domínios não fazem JOIN entre si, como o Element X.

### 6.3 ADR-003: o critério certo para um tier

O ADR-003 decide **api/impl**: "consumidor + (substituição ou fronteira de time)". O advogado do diabo apontou (A9) que o tier não passa nesse critério, porque separar telas de dados não é substituição nem fronteira de time.

**Correção:** são **duas decisões diferentes**, com critérios diferentes.

| Decisão | Pergunta | Critério |
|---|---|---|
| **Tier** (feature × domain) | Onde mora? | Domain se **2+ features** consomem, **ou** há consumidor sem UI, **ou** o dado precisa de dono único para invariantes/schema. Senão, `internal` na feature |
| **Par api/impl** (dentro da unidade) | Separar contrato de implementação? | Consumidor em outro módulo **e** (substituição de plataforma, vendor ou fake, **ou** fronteira de time) |

### 6.4 Detalhes corrigidos

- **São 35 arquivos, não 33.** 33 em commonMain + 2 em jvmMain.
- **"~9 ports" refinado (CAP-Y-32):**
  - **8 use cases + `DeepLinkShortcutManager` têm actuals** em Android, iOS e JVM: `GetDeepLinkHandlerIcon`, `GetDeepLinkHandlerInfo`, `GetDeepLinkHandlers`, `GetDeepLinkMetadata`, `LaunchDeepLink`, `PinDeepLinkToHomeScreen`, `ShareDeepLink` e `ValidateDeepLink`. Esses justificam interface.
  - **6 são só commonMain:** `DeleteAllDeepLinks`, `DeleteDeepLink`, `DuplicateDeepLink`, `GetAutoSuggestionLinks`, `GetDeepLinksAndFolderStream` e `LinkDeepLinkToFolder`. Não viram 6 interfaces. O destino depende de **onde está o consumidor**, porque uma classe concreta em `:domain:deeplink:impl` é invisível para quem está fora dele:
    - **Comandos no contrato do repositório** (`:domain:deeplink:api`): `DeleteDeepLink`, `DeleteAllDeepLinks` (usado por settings), `DuplicateDeepLink` e `LinkDeepLinkToFolder`. A implementação, com desativação de shortcuts e invariantes, fica em `impl`. Isso também resolve F1–F2.
    - **Port pequeno** `DeepLinkSuggestions` em `:domain:deeplink:api`, consumido por home: `GetAutoSuggestionLinks`. Uma classe concreta no api puxaria `core:preferences` para o contrato.
    - **Classe concreta `internal`** em `:feature:home:impl`, onde está o único consumidor: `GetDeepLinksAndFolderStream`.

    Classe concreta sem interface (modelo NiA, CAP-Y-31) só funciona quando consumidor e classe estão no mesmo módulo. O `core:domain` do NiA não tem split api/impl.
- **"Diverse capabilities" do Google não sustenta o nome.** O termo não aparece como justificativa de nome no estudo, mas fica registrado (CAP-N-11).
- **O argumento de build é mais fraco no iOS.** O incremental de Kotlin/Native é Beta e está desligado aqui (CAP-Y-33).

---

## 7. Aplicado a este repo

### 7.1 Destino de cada tipo de `feature:deeplink:api`

Resumo do inventário da análise do repo:

| Grupo | Tipos | Consumidores externos | Destino |
|---|---|---|---|
| Morto | `AnalyticsUserProperties` | nenhum | apagar |
| Domínio compartilhado | `DeepLink`, `Folder`, `Suggestion`, `DeepLinkRepository`, `FolderRepository` | home, settings, data-transfer, ui-component | `:domain:deeplink:api` (repositórios com comandos por ID) |
| Ports com actual por plataforma usados fora | `LaunchDeepLink`, `ValidateDeepLink` | home, settings, data-transfer | `:domain:deeplink:api` (interfaces) |
| Resolução de handler | `GetDeepLinkHandlerIcon`, `GetDeepLinkHandlerInfo`, `GetDeepLinkHandlers` + modelos `DeepLinkHandler`, `DeepLinkHandlerInfo` | nenhum **direto** hoje, mas `EnrichDeepLinksForList` (que vai para `:feature:deeplink:ui`, usado por home) precisa de ícone e info, e um widget também (s3) | port `DeepLinkHandlerResolver` em `:domain:deeplink:api` |
| Parsing | `ValidateDeepLink`, `GetDeepLinkMetadata` + modelo `DeepLinkMetadata` | data-transfer (Validate) | port `DeepLinkParser` em `:domain:deeplink:api` |
| Shortcuts | `DeepLinkShortcutManager`, `PinDeepLinkToHomeScreen` | nenhum direto, mas os comandos de delete (usados por settings) desativam shortcuts | port `DeepLinkShortcuts` em `:domain:deeplink:api` (fundidos); a impl de domínio chama o port nos deletes |
| Compartilhar | `ShareDeepLink` | nenhum fora de deeplink | actuals `internal` em `:feature:deeplink:impl` (regra §6.3: um consumidor só) |
| Comandos sem actual | `DeleteDeepLink`, `DeleteAllDeepLinks`, `DuplicateDeepLink`, `LinkDeepLinkToFolder` | settings (DeleteAll) | comandos no contrato do repositório em `:domain:deeplink:api`; implementação no impl (§6.4) |
| Query de uma tela só | `GetDeepLinksAndFolderStream` | só `HomeViewModel` | `:feature:home:impl`, `internal` |
| Sugestões | `GetAutoSuggestionLinks` | home | port `DeepLinkSuggestions` em `:domain:deeplink:api`; a impl lê a chave de preferência (ver R2 abaixo) |
| Apresentação compartilhada | `DeepLinkListItem`, `FolderListItem`, `DeepLinkFormatting`, `EnrichDeepLinksForList` | home, ui-component | `:feature:deeplink:ui` (ex-`ui-component`) |
| Apresentação de uma tela | `DeepLinkDetailsModel`, `EnrichDeepLinkForDetails` | nenhum | `:feature:deeplink:impl`, `internal` |
| Valor de port no pacote errado | `DeepLinkIcon` (id + `ByteArray`, sem Compose) | ui-component | `:domain:deeplink:api` (é dado, apesar do pacote `ui.model`) |
| Rotas | `DeepLinkRouteEntryPoint` | home, shared | `:feature:deeplink:api` (só keys) |
| JVM | `DeepLinkTarget` (expõe `DeviceBridge.Platform`), `DeepLinkTargetStateManager` | home (jvm) | `:domain:deeplink:api` jvmMain, com enum de plataforma próprio |
| Dimensão de analytics | `LaunchSource` | home, deeplink | decisão R8 abaixo |

### 7.2 Ordem

O advogado do diabo e o roteiro existente concordam nos passos 1 e 2. Divergem no 3: o advogado do diabo exige **medir** e só extrair quando um gatilho disparar, e contesta o gatilho 1 (§4.4) com o contra-exemplo do Element X. Este documento considera o gatilho 1 já satisfeito, e o objetivo de laboratório pesa a favor de extrair. **Para um produto real pequeno, parar no passo 2 e medir é uma escolha razoável.**

1. **Corrigir o contrato primeiro** (fase 1 do roteiro): F1–F3 e F6, com comandos atômicos por ID e repositórios `suspend`/`Flow`. Extrair antes faria a ABI mudar logo depois e recompilaria todos os consumidores duas vezes (A9).
2. **Podar o `:api` dentro da feature.** Este é o "B feito direito", e não é uma alternativa à extração: é a **primeira metade** dela.
   - apagar o tipo morto;
   - tornar `internal` o que só deeplink usa;
   - mover apresentação para `ui-component`;
   - trocar `DeviceBridge.Platform` por enum próprio;
   - ligar `abiValidation`.
3. **Extrair** para `:domain:deeplink:{api,impl,testing}`, com `ui-component` renomeado para `:feature:deeplink:ui`.
   - Com a poda feita, a extração é um move mecânico de cerca de 24 tipos para `:domain:deeplink:api`, fundidos em ~9 ports e contratos (§7.1), mais cerca de 50 arquivos de dados e plataforma do impl, mais o schema.
   - Crie `:domain:deeplink:testing` com fakes e um contract test que roda contra o SQL real (driver em memória) e contra a fake.
   - Preserve banco `dll-db`, pacote `dev.koga.deeplinklauncher.database`, classe `DeepLinkLauncherDatabase` e `1.sqm`. Valide com teste manual de upgrade.

### 7.3 Folders ficam na mesma unidade

Folders e deeplinks estão acoplados em todas as camadas:
- FK `deeplink.folderId → folder(id)`;
- `removeFolderFromDeeplinks` faz `UPDATE deeplink`;
- `DeepLink.sq` faz `LEFT JOIN folder`;
- `DeepLink` embute `folder: Folder?`;
- `upsertDeepLink` chama `upsertFolder` na mesma transação.

Um `:domain:folder` separado escreveria na tabela de outro domínio. Separe só depois de três mudanças: `DeepLink.folder` virar `folderId`, o SQL cruzado sumir, e o repositório de links parar de gravar pastas. A regra do Google cobre esse caso: "It can handle many types of data as long as they are related" (CAP-Y-34).

### 7.4 Decisões abertas que a análise encontrou

| # | Situação | Decisão necessária |
|---|---|---|
| R2 | `should_disable_deep_link_suggestions` é **escrita** por settings e **lida** pelo domínio (`GetAutoSuggestionLinksImpl`) | O domínio é dono da chave e expõe um setter, ou settings passa o valor. Chaves de feature saem do `Preferences` global |
| R3 | No desktop, `LaunchDeepLinkImpl.jvm.kt` manda **todo** `launch(url)` para o device selecionado, incluindo os links de GitHub/Play Store de settings (`SettingsViewModel.kt:30,37`) | Settings usa um `UrlOpener` próprio; `LaunchDeepLink` é só para deeplinks |
| R8 | Os eventos `deeplink_launched`, `deeplink_launch_failed`, `deeplink_created` e `favorite_toggled` estão definidos **duas vezes** (home e deeplink), ambos com `LaunchSource` | Tracking de lançamento vai para o domínio (`launch(deepLink, source)`) ou fica por feature com enum próprio |
| R9 | Shortcuts Android abrem `ACTION_VIEW` direto no app alvo, então **`recordLaunch` não roda** e o histórico perde esses lançamentos | Definir se histórico = tentativas via app ou inclui shortcuts (afeta s6) |
| — | Extensão ou widget iOS precisaria de **App Group**: o driver SQLite e o DataStore usam o sandbox padrão | Mudança em `core/database` e `core/preferences` (iosMain) antes de qualquer consumidor sem UI no iOS |

---

## 8. Regra de decisão para código novo

```
1. É tela ou fluxo de telas?                        → :feature:<x>:impl (keys em :feature:<x>:api)
2. Dado/regra usado só pelas telas de UMA feature?   → dentro de :feature:<x>:impl, internal
3. Dado/regra usado por 2+ features, OU por algo
   sem tela (widget, extensão, worker, CLI), OU que
   precisa de dono único (invariantes, schema)?      → :domain:<x>
4. Widget/mapper de entidade usado por 2+ features?  → :feature:<dona>:ui
5. Fala com SDK ou processo externo?                 → :integration:<x>
6. Serve a qualquer produto?                         → :core:<x>
Dentro da unidade: separar api/impl só com consumidor externo + (substituição ou fronteira de time).
```

Anti-padrões a vigiar:
- renomear um `:api` gordo para `:domain:x:api` sem desconstruí-lo (Dehghani);
- um único `:domain:app` com tudo (god domain);
- `domain:*:api` com Compose, navegação ou tipo de vendor;
- interface para use case sem actual nem fake;
- domain → domain fora da allowlist.

---

## Fontes principais

Todas com trechos citados e verificados em [2026-09-27-modularization-sources.md](2026-09-27-modularization-sources.md), Parte 2.

- Slack: *Scaling Slack's Mobile Codebases: Modularization*: <https://slack.engineering/stabilize-modularize-modernize-scaling-slacks-mobile-codebases-2/>
- Airbnb iOS: *Designing for Productivity in a Large-Scale iOS Application* (Wayback): <https://web.archive.org/web/2024id_/https://medium.com/airbnb-engineering/designing-for-productivity-in-a-large-scale-ios-application-9376a430a0bf>
- Google: padrões de modularização: <https://developer.android.com/topic/modularization/patterns>
- Now in Android: *Modularization Learning Journey*: <https://github.com/android/nowinandroid/blob/main/docs/ModularizationLearningJourney.md>
- Bitkey: <https://github.com/proto-at-block/bitkey/blob/main/app/domain/README.md>
- Wire Kalium: <https://github.com/wireapp/kalium/blob/develop/docs/ARCHITECTURE.md>
- Thunderbird: <https://github.com/thunderbird/thunderbird-android/blob/main/docs/architecture/module-organization.md> e ADR-0009
- DuckDuckGo: <https://github.com/duckduckgo/Android/blob/develop/.claude/docs/architecture.md>
- Element X: <https://github.com/element-hq/element-x-android/blob/develop/docs/_developer_onboarding.md>
- Duck Detector: <https://github.com/eltavine/Duck-Detector-Refactoring/blob/main/docs/architecture/README.md>
- Lewis & Fowler, *Microservices*: <https://martinfowler.com/articles/microservices.html>
- Richardson, *Decompose by business capability*: <https://microservices.io/patterns/decomposition/decompose-by-business-capability.html>
- Dehghani, *How to break a Monolith into Microservices*: <https://martinfowler.com/articles/break-monolith-into-microservices.html>
- Evans, *DDD Reference*: <https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf>
- Fowler, *PresentationDomainDataLayering*: <https://martinfowler.com/bliki/PresentationDomainDataLayering.html>
- Robert C. Martin, princípios de pacote: <http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod>

# Design System Document

## 1. Overview & Creative North Star: "The Modern Sertão"

This design system is a tribute to the resilient spirit of the Brazilian Northeast, translated into a high-end financial interface. We move away from the cold, sterile "blue-and-grey" of traditional banking to embrace **"The Modern Sertão"**—a creative North Star that combines the warmth of earthy textures with the precision of premium editorial layouts.

The experience is defined by **intentional asymmetry** and **tonal depth**. Rather than rigid grids that feel like templates, we utilize overlapping elements and large, expressive typography to create a sense of handcrafted authority. We celebrate the "cangaceiro" heritage not as a cliché, but as a subtle architectural motif—using the iconic curved arc of the *chapéu* to inform the geometry of containers and header treatments.

---

## 2. Colors: Earth, Sun, and Sophistication

The palette reflects the transition from the dry earth to the vibrant sunset. Every color has a functional role designed to guide the user's eye without visual noise.

### Palette Strategy
- **Primary (`#a20513` / Earth Red):** Reserved for high-stakes actions and crucial brand moments.
- **Secondary (`#9f4200` / Sunset Orange):** Used for dynamism, progress indicators, and active states.
- **Tertiary (`#6a4a00` / Dry Earth):** For grounding elements, inactive icons, and sophisticated secondary labels.
- **Backgrounds (`#fdf9f4` / Light Beige):** A warm, tactile base that reduces eye strain and feels more "affective" than pure white.

### The "No-Line" Rule
Standard 1px borders are strictly prohibited for sectioning. We define space through **Background Color Shifts**. Use `surface-container-low` to distinguish a sidebar from a `surface` main area. Boundaries must be felt, not seen.

### Surface Hierarchy & Nesting
Treat the UI as a series of physical layers. Use the `surface-container` tiers (Lowest to Highest) to create depth:
1. **Base Layer:** `surface` (#fdf9f4)
2. **Sectioning:** `surface-container-low` (#f7f3ee)
3. **Interactive Cards:** `surface-container-lowest` (#ffffff) to provide a subtle "lift" against the beige.

### Signature Textures: The "Glass & Gradient" Rule
To add soul, use subtle linear gradients transitioning from `primary` to `primary_container` for hero CTAs. Incorporate **Glassmorphism** for floating navigation bars or modal overlays using semi-transparent surface colors with a `backdrop-blur` of 12px–20px.

---

## 3. Typography: Editorial Authority

We use a high-contrast scale to move the UI into the realm of premium editorial design.

*   **Display & Headlines (Plus Jakarta Sans):** These are our "Statement" fonts. They provide a modern, wide stance that feels entrepreneurial and bold. 
    *   *Usage:* Use `display-lg` for hero balances or welcome messages to create an immediate impact.
*   **Titles & Body (Work Sans):** Chosen for its humanist qualities and exceptional legibility at small sizes.
    *   *Usage:* `body-lg` is the workhorse for all transactional data, ensuring the "professional" side of the brand is never compromised.

**The Typographic Soul:** Use a tight tracking (-2%) on `headline-lg` to create a compact, "premium print" look.

---

## 4. Elevation & Depth: Tonal Layering

Traditional drop shadows are often a crutch for poor layout. In this system, we achieve hierarchy through **Tonal Layering**.

*   **The Layering Principle:** Place a `surface-container-highest` element behind a `surface-container-lowest` card. The natural contrast creates a focal point without a single drop shadow.
*   **Ambient Shadows:** Where floating is required (e.g., FABs or high-priority modals), use a 15% opacity shadow tinted with `on-surface` (#1c1c19) and a massive blur (32px+). Avoid harsh "black" shadows.
*   **The "Ghost Border" Fallback:** If a border is required for accessibility, use the `outline-variant` token at **20% opacity**. It should be a whisper, not a shout.
*   **Curved Geometry:** To honor the *chapéu de cangaceiro*, use the `xl` (1.5rem) roundedness for top corners of cards and containers, creating a signature "arc" that echoes the brand logo.

---

## 5. Components: Handcrafted Precision

### Buttons
*   **Primary:** `primary` background with `on_primary` text. Use `lg` (1rem) rounding. For a premium touch, apply a subtle 10% `secondary` glow on hover.
*   **Tertiary:** No background, `primary` text. Use for low-emphasis actions like "Cancel" or "View Details."

### Cards & Lists
*   **Rule:** Forbid the use of divider lines. 
*   **Execution:** Separate list items using `spacing-4` (1rem) of vertical white space or by alternating background tones between `surface-container-low` and `surface-container-lowest`.

### Input Fields
*   **Style:** Minimalist. Use a `surface-container-high` background with no border. On focus, transition the background to `surface-container-lowest` and add a `primary` 2px bottom-stroke only.

### The "Hat" Motif (Signature Component)
*   Integrate the `chapéu` arc as a masked header image container or as a subtle "grainy" watermark in the `surface-container` of the user's dashboard profile.

---

## 6. Do's and Don'ts

### Do
*   **DO** use whitespace as a structural element. If a screen feels crowded, increase the spacing from `8` to `12`.
*   **DO** mix `display-sm` headlines with `body-md` text for a sophisticated editorial contrast.
*   **DO** use "Handcrafted" flat icons with slightly irregular, organic strokes to match the brand personality.

### Don't
*   **DON'T** use 100% opaque, high-contrast borders. It breaks the "Modern Sertão" warmth.
*   **DON'T** use standard Material Design blue for links. Everything must stay within the Earth/Sun palette.
*   **DON'T** crowd the "chapéu" icon. It is a symbol of resilience; give it room to breathe as a signature element, not a repeating pattern.
*   **DON'T** use sharp 0px corners. The brand is "Affective"—corners should always have at least `md` (0.75rem) rounding to feel welcoming.
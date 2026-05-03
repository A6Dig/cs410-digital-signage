import { useState, useCallback, useEffect, useRef } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Toolbar from "../components/Toolbar";
import SlidesPanel from "../components/SlidesPanel";
import LayoutRenderer from "../components/LayoutRenderer";
import PropertiesPanel from "../components/PropertiesPanel";
import "../styles/canvas.css";

const BASE_URL = "https://cs410-digital-signage.onrender.com";
const SLIDE_ROW_OFFSET = 100;

// ===== Layout Definitions =====
const LAYOUT_TEMPLATES = {
  single: [{ id: 1, contentType: "text", content: "" }],
  "two-columns": [
    { id: 1, contentType: "text", content: "" },
    { id: 2, contentType: "text", content: "" },
  ],
  "header-two-columns": [
    { id: 1, contentType: "text", content: "" },
    { id: 2, contentType: "text", content: "" },
    { id: 3, contentType: "text", content: "" },
  ],
  grid: [
    { id: 1, contentType: "text", content: "" },
    { id: 2, contentType: "text", content: "" },
    { id: 3, contentType: "text", content: "" },
    { id: 4, contentType: "text", content: "" },
  ],
  "right-column": [
    { id: 1, contentType: "text", content: "" },
    { id: 2, contentType: "text", content: "" },
    { id: 3, contentType: "text", content: "" },
    { id: 4, contentType: "text", content: "" },
  ],
  "bottom-row": [
    { id: 1, contentType: "text", content: "" },
    { id: 2, contentType: "text", content: "" },
    { id: 3, contentType: "text", content: "" },
    { id: 4, contentType: "text", content: "" },
  ],
  "six-section-grid": [
    { id: 1, contentType: "text", content: "" },
    { id: 2, contentType: "text", content: "" },
    { id: 3, contentType: "text", content: "" },
    { id: 4, contentType: "text", content: "" },
    { id: 5, contentType: "text", content: "" },
    { id: 6, contentType: "text", content: "" },
  ],
};

// Backend grid mapping (1-indexed positions as required by backend validation)
const TEMPLATE_GRID_MAP = {
  single: {
    cols: 1,
    rows: 1,
    slots: [{ colPos: 1, rowPos: 1, colSpan: 1, rowSpan: 1 }],
  },
  "two-columns": {
    cols: 2,
    rows: 1,
    slots: [
      { colPos: 1, rowPos: 1, colSpan: 1, rowSpan: 1 },
      { colPos: 2, rowPos: 1, colSpan: 1, rowSpan: 1 },
    ],
  },
  "header-two-columns": {
    cols: 2,
    rows: 2,
    slots: [
      { colPos: 1, rowPos: 1, colSpan: 2, rowSpan: 1 },
      { colPos: 1, rowPos: 2, colSpan: 1, rowSpan: 1 },
      { colPos: 2, rowPos: 2, colSpan: 1, rowSpan: 1 },
    ],
  },
  grid: {
    cols: 2,
    rows: 2,
    slots: [
      { colPos: 1, rowPos: 1, colSpan: 1, rowSpan: 1 },
      { colPos: 2, rowPos: 1, colSpan: 1, rowSpan: 1 },
      { colPos: 1, rowPos: 2, colSpan: 1, rowSpan: 1 },
      { colPos: 2, rowPos: 2, colSpan: 1, rowSpan: 1 },
    ],
  },
  "right-column": {
    cols: 2,
    rows: 3,
    slots: [
      { colPos: 1, rowPos: 1, colSpan: 1, rowSpan: 3 },
      { colPos: 2, rowPos: 1, colSpan: 1, rowSpan: 1 },
      { colPos: 2, rowPos: 2, colSpan: 1, rowSpan: 1 },
      { colPos: 2, rowPos: 3, colSpan: 1, rowSpan: 1 },
    ],
  },
  "bottom-row": {
    cols: 3,
    rows: 2,
    slots: [
      { colPos: 1, rowPos: 1, colSpan: 3, rowSpan: 1 },
      { colPos: 1, rowPos: 2, colSpan: 1, rowSpan: 1 },
      { colPos: 2, rowPos: 2, colSpan: 1, rowSpan: 1 },
      { colPos: 3, rowPos: 2, colSpan: 1, rowSpan: 1 },
    ],
  },
  "six-section-grid": {
    cols: 3,
    rows: 3,
    slots: [
      { colPos: 1, rowPos: 1, colSpan: 2, rowSpan: 2 },
      { colPos: 3, rowPos: 1, colSpan: 1, rowSpan: 1 },
      { colPos: 3, rowPos: 2, colSpan: 1, rowSpan: 1 },
      { colPos: 1, rowPos: 3, colSpan: 1, rowSpan: 1 },
      { colPos: 2, rowPos: 3, colSpan: 1, rowSpan: 1 },
      { colPos: 3, rowPos: 3, colSpan: 1, rowSpan: 1 },
    ],
  },
};

function detectTemplate(cols, rows, slotCount) {
  if (cols === 1 && rows === 1) return "single";
  if (cols === 2 && rows === 1) return "two-columns";
  if (cols === 2 && rows === 2 && slotCount === 3) return "header-two-columns";
  if (cols === 2 && rows === 2) return "grid";
  if (cols === 2 && rows === 3) return "right-column";
  if (cols === 3 && rows === 2) return "bottom-row";
  if (cols === 3 && rows === 3) return "six-section-grid";
  return "single";
}

const TINT_PRESETS = [
  { label: "None", value: null, color: "#e5e7eb" },
  { label: "Light Blue", value: "rgba(100, 149, 237, 0.35)", color: "rgb(100, 149, 237)" },
  { label: "Yellow", value: "rgba(255, 215, 0, 0.35)", color: "rgb(255, 215, 0)" },
  { label: "Black", value: "rgba(0, 0, 0, 0.35)", color: "rgb(0, 0, 0)" },
  { label: "Red", value: "rgba(220, 50, 50, 0.35)", color: "rgb(220, 50, 50)" },
  { label: "White", value: "rgba(255, 255, 255, 0.35)", color: "rgb(255, 255, 255)" },
];

function createSlide(layout = "single") {
  return {
    layout,
    sections: LAYOUT_TEMPLATES[layout].map((s) => ({ ...s })),
    backgroundImage: null,
    backgroundTint: null,
  };
}

function Canvas() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const layoutIdParam = searchParams.get("layoutId");

  const [slides, setSlides] = useState([createSlide("two-columns")]);
  const [currentSlideIndex, setCurrentSlideIndex] = useState(0);
  const [selectedSectionId, setSelectedSectionId] = useState(null);
  const [layoutDisplayName, setLayoutDisplayName] = useState("");
  const [saveStatus, setSaveStatus] = useState(null);
  const [isPreviewMode, setIsPreviewMode] = useState(false);

  const [savedLayoutId, setSavedLayoutId] = useState(null);
  const [savedModuleIds, setSavedModuleIds] = useState([]);

  const currentSlide = slides[currentSlideIndex];
  const bgInputRef = useRef(null);

  useEffect(() => {
    function loadFromLayout(layout) {
      const rawName = layout.name || "";
      const cleanName = rawName.includes("::")
        ? rawName
            .substring(0, rawName.lastIndexOf("::"))
            .replace(/\[\d+\]$/, "")
        : rawName;
      setLayoutDisplayName(cleanName);
      setSavedLayoutId(layout.id);

      const slots = layout.slots ?? [];
      if (slots.length === 0) {
        setSlides([createSlide("two-columns")]);
        setCurrentSlideIndex(0);
        setSavedModuleIds([]);
        console.log("Loaded empty layout, showing default slide");
        return;
      }

      const slideGroups = new Map();
      for (const slot of slots) {
        const slideIdx = Math.floor((slot.rowPos - 1) / SLIDE_ROW_OFFSET);
        if (!slideGroups.has(slideIdx)) slideGroups.set(slideIdx, []);
        slideGroups.get(slideIdx).push(slot);
      }

      const sortedIndices = [...slideGroups.keys()].sort((a, b) => a - b);
      const newSlides = [];
      const newModuleIds = [];

      for (const slideIdx of sortedIndices) {
        const slideSlots = slideGroups.get(slideIdx);
        slideSlots.sort((a, b) => (a.zIndex ?? 0) - (b.zIndex ?? 0));

        const firstConfig = slideSlots[0]?.module?.config;
        let template = firstConfig?.template;
        if (!template || !LAYOUT_TEMPLATES[template]) {
          template = detectTemplate(
            layout.cols,
            layout.rows,
            slideSlots.length
          );
        }

        const templateSections = LAYOUT_TEMPLATES[template];
        const sections = templateSections.map((defaultSection, i) => {
          const slot = slideSlots[i];
          const config = slot?.module?.config;
          if (config && (config.contentType || config.content)) {
            return {
              ...defaultSection,
              contentType: config.contentType || defaultSection.contentType,
              content: config.content || "",
            };
          }
          return { ...defaultSection };
        });

        const bgImage = firstConfig?.backgroundImage || null;
        const bgTint = firstConfig?.backgroundTint || null;
        newSlides.push({ layout: template, sections, backgroundImage: bgImage, backgroundTint: bgTint });
        newModuleIds.push(slideSlots.map((s) => s.module?.id ?? null));
      }

      setSlides(newSlides);
      setCurrentSlideIndex(0);
      setSelectedSectionId(null);
      setSavedModuleIds(newModuleIds);

      console.log(
        `Loaded ${newSlides.length} slide(s) from layout ${layout.id}`
      );
    }

    if (layoutIdParam) {
      fetch(`${BASE_URL}/api/layouts/${layoutIdParam}`)
        .then((r) => r.json())
        .then((res) => {
          if (res.data) loadFromLayout(res.data);
        })
        .catch((err) => console.error("Failed to load layout:", err));
    } else {
      fetch(`${BASE_URL}/api/layouts`)
        .then((r) => r.json())
        .then((res) => {
          const layouts = res.data ?? [];
          if (layouts.length > 1) {
            console.warn(
              `Found ${layouts.length} layouts. IDs: ${layouts.map((l) => l.id).join(", ")}. ` +
                `These may include orphaned layouts from previous saves. Loading the most recent one (ID: ${layouts[layouts.length - 1].id}).`
            );
          }
          if (layouts.length > 0) loadFromLayout(layouts[layouts.length - 1]);
        })
        .catch((err) => console.error("Failed to load layouts:", err));
    }
  }, [layoutIdParam]);

  // ===== Save — persist all slides into one layout =====
  const handleSave = useCallback(async () => {
    setSaveStatus("saving");

    try {
      const newModuleIds = [];
      for (let slideIdx = 0; slideIdx < slides.length; slideIdx++) {
        const slide = slides[slideIdx];
        const existingModules = savedModuleIds[slideIdx] || [];
        const slideModuleIds = await Promise.all(
          slide.sections.map(async (section, i) => {
            const moduleData = {
              name: `slide-${slideIdx}-section-${i + 1}`,
              type: "CLOCK",
              config: {
                slideIndex: slideIdx,
                template: slide.layout,
                contentType: section.contentType,
                content: section.content,
                ...(i === 0 && {
                  backgroundImage: slide.backgroundImage,
                  backgroundTint: slide.backgroundTint,
                }),
              },
              adCollection: null,
            };

            const existingId = existingModules[i];
            if (existingId) {
              const res = await fetch(`${BASE_URL}/api/modules/${existingId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(moduleData),
              });
              if (!res.ok)
                throw new Error(
                  `Module update failed for slide ${slideIdx}, section ${i}`
                );
              const data = await res.json();
              return data.data?.id ?? existingId;
            }

            const res = await fetch(`${BASE_URL}/api/modules`, {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(moduleData),
            });
            if (!res.ok)
              throw new Error(
                `Module creation failed for slide ${slideIdx}, section ${i}`
              );
            const data = await res.json();
            return data.data?.id;
          })
        );
        newModuleIds.push(slideModuleIds);
      }

      if (savedLayoutId) {
        await fetch(`${BASE_URL}/api/layouts/${savedLayoutId}/slots/all`, {
          method: "DELETE",
        });
      }

      const allSlots = [];
      for (let slideIdx = 0; slideIdx < slides.length; slideIdx++) {
        const slide = slides[slideIdx];
        const gridInfo =
          TEMPLATE_GRID_MAP[slide.layout] || TEMPLATE_GRID_MAP.single;
        gridInfo.slots.forEach((slotPos, i) => {
          allSlots.push({
            moduleId: newModuleIds[slideIdx][i] ?? null,
            colPos: slotPos.colPos,
            rowPos: slideIdx * SLIDE_ROW_OFFSET + slotPos.rowPos,
            colSpan: slotPos.colSpan,
            rowSpan: slotPos.rowSpan,
            zIndex: slideIdx * SLIDE_ROW_OFFSET + i + 1,
          });
        });
      }

      const firstGrid =
        TEMPLATE_GRID_MAP[slides[0].layout] || TEMPLATE_GRID_MAP.single;
      const layoutBody = {
        name: layoutDisplayName || "Canvas",
        cols: firstGrid.cols,
        rows: firstGrid.rows,
        slots: allSlots,
      };

      console.log(
        `Save payload: ${slides.length} slide(s), ${allSlots.length} total slots`,
        layoutBody
      );

      let layoutRes;
      if (savedLayoutId) {
        layoutRes = await fetch(`${BASE_URL}/api/layouts/${savedLayoutId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(layoutBody),
        });
      } else {
        layoutRes = await fetch(`${BASE_URL}/api/layouts`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(layoutBody),
        });
      }
      if (!layoutRes.ok) throw new Error("Layout save failed");
      const layoutData = await layoutRes.json();

      setSavedLayoutId(layoutData.data?.id);
      setSavedModuleIds(newModuleIds);

      console.log(
        `Saved ${slides.length} slide(s) with ${allSlots.length} total slots to layout ${layoutData.data?.id}`
      );
      setSaveStatus("saved");
      setTimeout(() => setSaveStatus(null), 2000);
    } catch (err) {
      console.error("Save error:", err);
      setSaveStatus("error");
      setTimeout(() => setSaveStatus(null), 3000);
    }
  }, [slides, layoutDisplayName, savedLayoutId, savedModuleIds]);

  // ===== Slide Operations =====
  const addSlide = useCallback(() => {
    setSlides((prev) => [...prev, createSlide("single")]);
    setCurrentSlideIndex(slides.length);
    setSelectedSectionId(null);
  }, [slides.length]);

  const switchSlide = useCallback((index) => {
    setCurrentSlideIndex(index);
    setSelectedSectionId(null);
  }, []);

  const deleteSlide = useCallback(
    (index) => {
      if (slides.length <= 1) return;
      setSlides((prev) => prev.filter((_, i) => i !== index));
      setSavedModuleIds((prev) => prev.filter((_, i) => i !== index));
      setCurrentSlideIndex((prev) => {
        if (prev >= slides.length - 1) return Math.max(0, slides.length - 2);
        if (index <= prev) return Math.max(0, prev - 1);
        return prev;
      });
      setSelectedSectionId(null);
    },
    [slides.length]
  );

  // ===== Layout Change =====
  const changeLayout = useCallback(
    (layout) => {
      setSlides((prev) =>
        prev.map((slide, i) =>
          i === currentSlideIndex
            ? {
                ...slide,
                layout,
                sections: LAYOUT_TEMPLATES[layout].map((s) => ({ ...s })),
              }
            : slide
        )
      );
      setSelectedSectionId(null);
    },
    [currentSlideIndex]
  );

  // ===== Section Selection =====
  const selectSection = useCallback((sectionId) => {
    setSelectedSectionId(sectionId);
  }, []);

  // ===== Section Update (from PropertiesPanel) =====
  const updateSection = useCallback(
    (sectionId, changes) => {
      setSlides((prev) =>
        prev.map((slide, i) =>
          i === currentSlideIndex
            ? {
                ...slide,
                sections: slide.sections.map((sec) =>
                  sec.id === sectionId ? { ...sec, ...changes } : sec
                ),
              }
            : slide
        )
      );
    },
    [currentSlideIndex]
  );

  // ===== Slide Reorder =====
  const reorderSlides = useCallback((fromIndex, toIndex) => {
    const reorder = (prev) => {
      const updated = [...prev];
      const [moved] = updated.splice(fromIndex, 1);
      updated.splice(toIndex, 0, moved);
      return updated;
    };
    setSlides(reorder);
    setSavedModuleIds(reorder);
    setCurrentSlideIndex((prev) => {
      if (prev === fromIndex) return toIndex;
      if (fromIndex < prev && toIndex >= prev) return prev - 1;
      if (fromIndex > prev && toIndex <= prev) return prev + 1;
      return prev;
    });
  }, []);

  const handlePreview = useCallback(() => {
    setIsPreviewMode(true);
  }, []);

  useEffect(() => {
    if (!isPreviewMode) return;
    const onKey = (e) => {
      if (e.key === "Escape") setIsPreviewMode(false);
    };
    document.addEventListener("keydown", onKey);
    return () => document.removeEventListener("keydown", onKey);
  }, [isPreviewMode]);

  const selectedSection = currentSlide
    ? currentSlide.sections.find((s) => s.id === selectedSectionId)
    : null;

  const handleBgUpload = useCallback((e) => {
    const file = e.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = () => {
      setSlides((prev) =>
        prev.map((slide, i) =>
          i === currentSlideIndex
            ? { ...slide, backgroundImage: reader.result }
            : slide
        )
      );
    };
    reader.readAsDataURL(file);
    e.target.value = "";
  }, [currentSlideIndex]);

  const handleRemoveBg = useCallback(() => {
    setSlides((prev) =>
      prev.map((slide, i) =>
        i === currentSlideIndex
          ? { ...slide, backgroundImage: null }
          : slide
      )
    );
  }, [currentSlideIndex]);

  const handleTintChange = useCallback((tintValue) => {
    setSlides((prev) =>
      prev.map((slide, i) => {
        if (i !== currentSlideIndex) return slide;
        if (tintValue === null) return { ...slide, backgroundTint: null };
        return {
          ...slide,
          backgroundTint: slide.backgroundTint === tintValue ? null : tintValue,
        };
      })
    );
  }, [currentSlideIndex]);

  return (
    <div className="editor">
      <Toolbar
        currentLayout={currentSlide?.layout}
        onAddSlide={addSlide}
        onChangeLayout={changeLayout}
        onPreview={handlePreview}
        onGoToLayouts={() => navigate("/layouts")}
        onSave={handleSave}
        saveStatus={saveStatus}
      />

      <div className="editor-body">
        <SlidesPanel
          slides={slides}
          currentSlideIndex={currentSlideIndex}
          onSwitchSlide={switchSlide}
          onAddSlide={addSlide}
          onDeleteSlide={deleteSlide}
          onReorderSlides={reorderSlides}
        />

        <div className="canvas-area">
          <div className="canvas-frame">
            {currentSlide && (
              <LayoutRenderer
                layout={currentSlide.layout}
                slots={currentSlide.sections}
                selectedSectionId={selectedSectionId}
                onSelectSection={selectSection}
                rows={TEMPLATE_GRID_MAP[currentSlide.layout]?.rows ?? 1}
                cols={TEMPLATE_GRID_MAP[currentSlide.layout]?.cols ?? 1}
                gridSlots={TEMPLATE_GRID_MAP[currentSlide.layout]?.slots ?? []}
                backgroundImage={currentSlide.backgroundImage}
                backgroundTint={currentSlide.backgroundTint}
              />
            )}
          </div>
          {currentSlide && (
            <div className="bg-controls">
              <div className="bg-controls-row">
                <input
                  ref={bgInputRef}
                  type="file"
                  accept="image/*"
                  onChange={handleBgUpload}
                  style={{ display: "none" }}
                />
                <button onClick={() => bgInputRef.current?.click()}>
                  Upload Background
                </button>
                {currentSlide.backgroundImage && (
                  <>
                    <img
                      src={currentSlide.backgroundImage}
                      alt="Background preview"
                      className="bg-thumb"
                    />
                    <button onClick={handleRemoveBg}>Remove</button>
                  </>
                )}
              </div>
              <div className="bg-controls-row">
                <span className="bg-label">Tint:</span>
                {TINT_PRESETS.map((preset) => (
                  <button
                    key={preset.label}
                    className={`tint-swatch${
                      currentSlide.backgroundTint === preset.value
                        ? " tint-swatch-active"
                        : ""
                    }`}
                    style={{ backgroundColor: preset.color }}
                    title={preset.label}
                    onClick={() => handleTintChange(preset.value)}
                  />
                ))}
              </div>
            </div>
          )}
        </div>

        <PropertiesPanel
          section={selectedSection}
          onUpdate={updateSection}
        />
      </div>

      {isPreviewMode && currentSlide && (
        <div className="preview-overlay">
          <button
            className="preview-exit"
            onClick={() => setIsPreviewMode(false)}
          >
            Exit Preview
          </button>
          <div className="preview-canvas">
            <LayoutRenderer
              layout={currentSlide.layout}
              slots={currentSlide.sections}
              selectedSectionId={null}
              onSelectSection={() => {}}
              rows={TEMPLATE_GRID_MAP[currentSlide.layout]?.rows ?? 1}
              cols={TEMPLATE_GRID_MAP[currentSlide.layout]?.cols ?? 1}
              gridSlots={TEMPLATE_GRID_MAP[currentSlide.layout]?.slots ?? []}
              backgroundImage={currentSlide.backgroundImage}
              backgroundTint={currentSlide.backgroundTint}
            />
          </div>
        </div>
      )}
    </div>
  );
}

export default Canvas;

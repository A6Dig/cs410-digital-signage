import { useState, useCallback, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Toolbar from "../components/Toolbar";
import SlidesPanel from "../components/SlidesPanel";
import LayoutRenderer from "../components/LayoutRenderer";
import PropertiesPanel from "../components/PropertiesPanel";
import "../styles/canvas.css";

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

function parseLayoutName(name) {
  if (!name) return { displayName: "", template: null };
  const idx = name.lastIndexOf("::");
  if (idx === -1) return { displayName: name, template: null };
  return {
    displayName: name.substring(0, idx),
    template: name.substring(idx + 2),
  };
}

// Layout.name is VARCHAR(50) — keep the encoded name within that limit
function encodeLayoutName(displayName, template) {
  const suffix = `::${template}`;
  const maxDisplayLen = 50 - suffix.length;
  const truncated =
    displayName.length > maxDisplayLen
      ? displayName.substring(0, maxDisplayLen)
      : displayName;
  return `${truncated}${suffix}`;
}

function createSlide(layout = "single") {
  return {
    layout,
    sections: LAYOUT_TEMPLATES[layout].map((s) => ({ ...s })),
  };
}

function Canvas() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const layoutIdParam = searchParams.get("layoutId");

  const [slides, setSlides] = useState([createSlide("two-columns")]);
  const [currentSlideIndex, setCurrentSlideIndex] = useState(0);
  const [selectedSectionId, setSelectedSectionId] = useState(null);
  const [activeLayoutId, setActiveLayoutId] = useState(layoutIdParam);
  const [layoutDisplayName, setLayoutDisplayName] = useState("");
  const [savedSlotIds, setSavedSlotIds] = useState([]);
  const [savedModuleIds, setSavedModuleIds] = useState([]);
  const [saveStatus, setSaveStatus] = useState(null);

  const currentSlide = slides[currentSlideIndex];

  useEffect(() => {
    function loadLayout(layout) {
      const { displayName, template: savedTemplate } = parseLayoutName(
        layout.name
      );
      setLayoutDisplayName(displayName || layout.name);
      setActiveLayoutId(layout.id);

      const slots = layout.slots ?? [];
      const sortedSlots = [...slots].sort(
        (a, b) => (a.zIndex ?? 0) - (b.zIndex ?? 0)
      );
      const slotCount = sortedSlots.length;
      const template =
        savedTemplate && LAYOUT_TEMPLATES[savedTemplate]
          ? savedTemplate
          : detectTemplate(layout.cols, layout.rows, slotCount);

      // Rebuild sections from template, restoring saved content from module.config
      const templateSections = LAYOUT_TEMPLATES[template];
      const sections = templateSections.map((defaultSection, i) => {
        const slot = sortedSlots[i];
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

      setSlides([{ layout: template, sections }]);
      setCurrentSlideIndex(0);
      setSelectedSectionId(null);
      setSavedSlotIds(sortedSlots.map((s) => s.id));
      setSavedModuleIds(sortedSlots.map((s) => s.module?.id ?? null));
    }

    if (layoutIdParam) {
      fetch(`/api/layouts/${layoutIdParam}`)
        .then((r) => r.json())
        .then((res) => {
          if (res.data) loadLayout(res.data);
        })
        .catch((err) => console.error("Failed to load layout:", err));
    } else {
      fetch("/api/layouts")
        .then((r) => r.json())
        .then((res) => {
          const layouts = res.data ?? [];
          if (layouts.length > 0) loadLayout(layouts[layouts.length - 1]);
        })
        .catch((err) => console.error("Failed to load layouts:", err));
    }
  }, [layoutIdParam]);

  // ===== Save — persist modules (content) then layout (structure) =====
  const handleSave = useCallback(async () => {
    if (!activeLayoutId) return;
    setSaveStatus("saving");

    try {
      const slide = slides[currentSlideIndex] || slides[0];
      const gridInfo =
        TEMPLATE_GRID_MAP[slide.layout] || TEMPLATE_GRID_MAP.single;

      // Step 1: create or update a Module for each section
      const moduleIds = await Promise.all(
        slide.sections.map(async (section, i) => {
          const moduleData = {
            name: `section-${i + 1}`,
            type: "CLOCK",
            config: {
              contentType: section.contentType,
              content: section.content,
            },
            adCollection: null,
          };

          const existingId = savedModuleIds[i];
          if (existingId) {
            const res = await fetch(`/api/modules/${existingId}`, {
              method: "PUT",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(moduleData),
            });
            if (!res.ok) throw new Error(`Module update failed for section ${i}`);
            const data = await res.json();
            return data.data?.id ?? existingId;
          }

          const res = await fetch("/api/modules", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(moduleData),
          });
          if (!res.ok) throw new Error(`Module creation failed for section ${i}`);
          const data = await res.json();
          return data.data?.id;
        })
      );

      // Step 2: save the layout with slot positions + module references
      const slots = gridInfo.slots.map((slotPos, i) => ({
        id: i < savedSlotIds.length ? savedSlotIds[i] : null,
        moduleId: moduleIds[i] ?? null,
        colPos: slotPos.colPos,
        rowPos: slotPos.rowPos,
        colSpan: slotPos.colSpan,
        rowSpan: slotPos.rowSpan,
        zIndex: i + 1,
      }));

      const body = {
        name: encodeLayoutName(
          layoutDisplayName || slide.layout,
          slide.layout
        ),
        cols: gridInfo.cols,
        rows: gridInfo.rows,
        slots,
      };

      const layoutRes = await fetch(`/api/layouts/${activeLayoutId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });
      if (!layoutRes.ok) throw new Error("Layout save failed");
      const layoutData = await layoutRes.json();

      // Update tracked IDs from the response
      if (layoutData.data?.slots) {
        const returnedSlots = [...layoutData.data.slots].sort(
          (a, b) => (a.zIndex ?? 0) - (b.zIndex ?? 0)
        );
        setSavedSlotIds(returnedSlots.map((s) => s.id));
        setSavedModuleIds(returnedSlots.map((s) => s.module?.id ?? null));
      }

      setSaveStatus("saved");
      setTimeout(() => setSaveStatus(null), 2000);
    } catch (err) {
      console.error("Save error:", err);
      setSaveStatus("error");
      setTimeout(() => setSaveStatus(null), 3000);
    }
  }, [
    activeLayoutId,
    slides,
    currentSlideIndex,
    layoutDisplayName,
    savedSlotIds,
    savedModuleIds,
  ]);

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
    setSlides((prev) => {
      const updated = [...prev];
      const [moved] = updated.splice(fromIndex, 1);
      updated.splice(toIndex, 0, moved);
      return updated;
    });
    setCurrentSlideIndex((prev) => {
      if (prev === fromIndex) return toIndex;
      if (fromIndex < prev && toIndex >= prev) return prev - 1;
      if (fromIndex > prev && toIndex <= prev) return prev + 1;
      return prev;
    });
  }, []);

  // ===== Preview (stub) =====
  const handlePreview = useCallback(() => {
    console.log("Preview slides:", JSON.stringify(slides, null, 2));
  }, [slides]);

  const selectedSection = currentSlide
    ? currentSlide.sections.find((s) => s.id === selectedSectionId)
    : null;

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
                sections={currentSlide.sections}
                selectedSectionId={selectedSectionId}
                onSelectSection={selectSection}
              />
            )}
          </div>
        </div>

        <PropertiesPanel
          section={selectedSection}
          onUpdate={updateSection}
        />
      </div>
    </div>
  );
}

export default Canvas;

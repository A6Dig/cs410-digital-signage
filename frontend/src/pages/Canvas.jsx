import { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import Toolbar from "../components/Toolbar";
import SlidesPanel from "../components/SlidesPanel";
import LayoutRenderer from "../components/LayoutRenderer";
import PropertiesPanel from "../components/PropertiesPanel";
import { layoutService } from "../api/layoutService";
import "../styles/canvas.css";

// ===== Layout Definitions =====
// Maps each layout name to its default sections
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

// Helper to create a fresh slide
function createSlide(layout = "single") {
  return {
    layout,
    sections: LAYOUT_TEMPLATES[layout].map((s) => ({ ...s })),
  };
}

// Grid dimensions the backend expects per LayoutDtoBase (cols/rows ints).
// The frontend's named layouts are mapped to a coarse 12x8 grid so each
// section becomes a LayoutSlotRequestDto with colPos/rowPos/colSpan/rowSpan.
const GRID_COLS = 12;
const GRID_ROWS = 8;

// Maps a frontend slide to the backend LayoutRequestDto<LayoutSlotRequestDto>
// shape documented in backend/src/main/java/com/a6dig/digitalsignage/dto/.
function slideToLayoutRequest(slide, index) {
  const count = slide.sections.length || 1;
  const colSpan = Math.max(1, Math.floor(GRID_COLS / count));
  return {
    name: `${slide.layout}-${index + 1}`,
    cols: GRID_COLS,
    rows: GRID_ROWS,
    slots: slide.sections.map((sec, i) => ({
      // PENDING BACKEND SUPPORT: moduleId must reference an existing Module
      // (see ModuleController). Until modules are created from the UI we
      // send 0 as a placeholder; the backend will reject this until real
      // module ids are wired through.
      moduleId: 0,
      colPos: i * colSpan,
      rowPos: 0,
      colSpan,
      rowSpan: GRID_ROWS,
      zIndex: 0,
    })),
  };
}

// ===== Canvas Page — Main Editor =====
function Canvas() {
  const navigate = useNavigate();

  const [slides, setSlides] = useState([createSlide("two-columns")]);
  const [currentSlideIndex, setCurrentSlideIndex] = useState(0);
  const [selectedSectionId, setSelectedSectionId] = useState(null);
  const [loadError, setLoadError] = useState("");
  const [saveStatus, setSaveStatus] = useState("");

  // Fetch existing layouts on mount so the editor reflects persisted state.
  // The backend returns LayoutResponseDto { id, name, cols, rows, slots, ... }.
  // PENDING BACKEND SUPPORT: the backend Layout model is a grid of slots,
  // not a sequence of slides. For now we load the list only to prove the
  // connection and surface errors; individual slide editing still runs on
  // local state until a slide/layout mapping is agreed with the backend.
  useEffect(() => {
    let cancelled = false;
    layoutService
      .list()
      .then((layouts) => {
        if (cancelled) return;
        // eslint-disable-next-line no-console
        console.log("Fetched layouts from backend:", layouts);
      })
      .catch((err) => {
        if (cancelled) return;
        setLoadError(err.message || "Failed to load layouts");
      });
    return () => {
      cancelled = true;
    };
  }, []);

  const currentSlide = slides[currentSlideIndex];

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
      // FUTURE: Debounce and auto-save via API
      // fetch(`/api/slides/${slideId}/sections/${sectionId}`, { method: "PATCH", ... })
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
  const reorderSlides = useCallback(
    (fromIndex, toIndex) => {
      setSlides((prev) => {
        const updated = [...prev];
        const [moved] = updated.splice(fromIndex, 1);
        updated.splice(toIndex, 0, moved);
        return updated;
      });
      // Adjust currentSlideIndex to follow the moved slide
      setCurrentSlideIndex((prev) => {
        if (prev === fromIndex) return toIndex;
        if (fromIndex < prev && toIndex >= prev) return prev - 1;
        if (fromIndex > prev && toIndex <= prev) return prev + 1;
        return prev;
      });
    },
    []
  );

  // ===== Preview (stub) =====
  const handlePreview = useCallback(() => {
    console.log("Preview slides:", JSON.stringify(slides, null, 2));
  }, [slides]);

  // Get currently selected section data for the properties panel
  const selectedSection = currentSlide
    ? currentSlide.sections.find((s) => s.id === selectedSectionId)
    : null;

  return (
    <div className="editor">
      {/* Top Toolbar */}
      <Toolbar
        currentLayout={currentSlide?.layout}
        onAddSlide={addSlide}
        onChangeLayout={changeLayout}
        onPreview={handlePreview}
        onLogout={() => navigate("/")}
      />

      <div className="editor-body">
        {/* Left — Slides Panel */}
        <SlidesPanel
          slides={slides}
          currentSlideIndex={currentSlideIndex}
          onSwitchSlide={switchSlide}
          onAddSlide={addSlide}
          onDeleteSlide={deleteSlide}
          onReorderSlides={reorderSlides}
        />

        {/* Center — Canvas */}
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

        {/* Right — Properties Panel */}
        <PropertiesPanel
          section={selectedSection}
          onUpdate={updateSection}
        />
      </div>
    </div>
  );
}

export default Canvas;

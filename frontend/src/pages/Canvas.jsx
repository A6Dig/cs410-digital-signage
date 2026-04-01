import { useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import Toolbar from "../components/Toolbar";
import SlidesPanel from "../components/SlidesPanel";
import LayoutRenderer from "../components/LayoutRenderer";
import PropertiesPanel from "../components/PropertiesPanel";
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
};

// Helper to create a fresh slide
function createSlide(layout = "single") {
  return {
    layout,
    sections: LAYOUT_TEMPLATES[layout].map((s) => ({ ...s })),
  };
}

// ===== Canvas Page — Main Editor =====
function Canvas() {
  const navigate = useNavigate();

  // FUTURE: Load slides from API on mount
  // useEffect(() => { fetch("/api/slides").then(...) }, []);
  const [slides, setSlides] = useState([createSlide("two-columns")]);
  const [currentSlideIndex, setCurrentSlideIndex] = useState(0);
  const [selectedSectionId, setSelectedSectionId] = useState(null);

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

import { useState, useCallback, useEffect, useRef } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Toolbar from "../components/Toolbar";
import SlidesPanel from "../components/SlidesPanel";
import LayoutRenderer from "../components/LayoutRenderer";
import PropertiesPanel from "../components/PropertiesPanel";
import "../styles/canvas.css";

// ===== Layout Definitions =====
// Maps each layout name to its default sections
// const LAYOUT_TEMPLATES = {
//   single: [{ id: 1, contentType: "text", content: "" }],
//   "two-columns": [
//     { id: 1, contentType: "text", content: "" },
//     { id: 2, contentType: "text", content: "" },
//   ],
//   "header-two-columns": [
//     { id: 1, contentType: "text", content: "" },
//     { id: 2, contentType: "text", content: "" },
//     { id: 3, contentType: "text", content: "" },
//   ],
//   grid: [
//     { id: 1, contentType: "text", content: "" },
//     { id: 2, contentType: "text", content: "" },
//     { id: 3, contentType: "text", content: "" },
//     { id: 4, contentType: "text", content: "" },
//   ],
//   "right-column": [
//     { id: 1, contentType: "text", content: "" },
//     { id: 2, contentType: "text", content: "" },
//     { id: 3, contentType: "text", content: "" },
//     { id: 4, contentType: "text", content: "" },
//   ],
//   "bottom-row": [
//     { id: 1, contentType: "text", content: "" },
//     { id: 2, contentType: "text", content: "" },
//     { id: 3, contentType: "text", content: "" },
//     { id: 4, contentType: "text", content: "" },
//   ],
//   "six-section-grid": [
//     { id: 1, contentType: "text", content: "" },
//     { id: 2, contentType: "text", content: "" },
//     { id: 3, contentType: "text", content: "" },
//     { id: 4, contentType: "text", content: "" },
//     { id: 5, contentType: "text", content: "" },
//     { id: 6, contentType: "text", content: "" },
//   ],
// };

// Helper to create a fresh slide
// function createSlide(layout = "single") {
//   return {
//     layout,
//     sections: LAYOUT_TEMPLATES[layout].map((s) => ({ ...s })),
//   };
// }

// ===== Canvas Page — Main Editor =====
function Canvas() {
    const [layout, setLayout] = useState({
        name:"Main Layout",
        cols: 1,
        rows: 1,
        id: null,
        slots: [
            {id: null, colPos: 1, rowPos: 1, colSpan: 1, rowSpan: 1, zIndex: 1}
        ]

      });

  const [searchParams] = useSearchParams();
  const layoutId = searchParams.get("layoutId");
  const [loading, setLoading] = useState(true);
  const hasFetched = useRef(false);


  const handleSave = async () => {
    try {
    console.log(JSON.stringify(layout))
     const res = await fetch(`/api/layouts/${layoutId}/slots`, {
       method: "POST",
       headers: {
         "Content-Type": "application/json",
       },
       body: JSON.stringify(layout),
     });

     if (!res.ok) {
       throw new Error("Failed to save slot");
     }

     const data = await res.json();
     return data;
    } catch (err) {
     console.error("Slot save error:", err);
     return null;
    }
  };

  useEffect(() => {
      if (hasFetched.current) return;
      if (!layoutId) return;

      hasFetched.current = true;

      fetch(`/api/layouts/${layoutId}`)
        .then(r => r.json())
        .then(res => {
          const data = res.data || {};

          setLayout(prev => ({
            ...prev,
            id: data.id ?? prev.id,
            cols: data.cols ?? prev.cols,
            rows: data.rows ?? prev.rows,
            slots: Array.isArray(data.slots) && data.slots.length
              ? data.slots
              : buildSlots(data.rows ?? prev.rows, data.cols ?? prev.cols),
          }));
        })
        .finally(() => setLoading(false));
    }, [layoutId]);

    const buildSlots = (rows, cols, prevSlots = []) => {
      const map = new Map();
      prevSlots.forEach((s) => {
        map.set(`${s.rowPos}-${s.colPos}`, s);
      });

      const slots = [];

      for (let r = 1; r <= rows; r++) {
        for (let c = 1; c <= cols; c++) {
          const key = `${r}-${c}`;
          slots.push(
            map.get(key) || {
              id: null,
              rowPos: r,
              colPos: c,
              rowSpan: 1,
              colSpan: 1,
              zIndex: 1,
              contentType: "text",
              content: ""
            }
          );
        }
      }

      return slots;
    };
  const addRow = useCallback(() => {
      setLayout((prev) => {
        const rows = prev.rows + 1;
        return {
          ...prev,
          rows,
          slots: buildSlots(rows, prev.cols, prev.slots),
        };
      });

  },[]);

  const removeRow = useCallback(() => {
      setLayout((prev) => {
        if (prev.rows <= 1) return prev;

        const rows = prev.rows - 1;

        return {
          ...prev,
          rows,
          slots: buildSlots(rows, prev.cols, prev.slots),
        };
      });
  },[]);


  const addColumn = useCallback(() => {
      setLayout((prev) => {
        const cols = prev.cols + 1;

        return {
          ...prev,
          cols,
          slots: buildSlots(prev.rows, cols, prev.slots),
        };
      });
  },[]);


  const removeColumn = useCallback(() => {
      setLayout((prev) => {
        if (prev.cols <= 1) return prev;

        const cols = prev.cols - 1;

        return {
          ...prev,
          cols,
          slots: buildSlots(prev.rows, cols, prev.slots),
        };
      });

  },[]);

  useEffect(() => {
    console.log(layout);
  }, [layout]);

  const navigate = useNavigate();
//
//   // FUTURE: Load slides from API on mount
//   // useEffect(() => { fetch("/api/slides").then(...) }, []);
//   const [slides, setSlides] = useState([createSlide("two-columns")]);
  const [currentSlideIndex, setCurrentSlideIndex] = useState(0);
  const [selectedSectionId, setSelectedSectionId] = useState(null);
//
//   const currentSlide = slides[currentSlideIndex];
//
//   // ===== Slide Operations =====
  const addSlide = useCallback(() => {
//     setSlides((prev) => [...prev, createSlide("single")]);
//     setCurrentSlideIndex(slides.length);
//     setSelectedSectionId(null);
  }, []);
//
  const switchSlide = useCallback((index) => {
//     setCurrentSlideIndex(index);
//     setSelectedSectionId(null);
  }, []);
//
  const deleteSlide = useCallback(
    (index) => {
//       if (slides.length <= 1) return;
//       setSlides((prev) => prev.filter((_, i) => i !== index));
//       setCurrentSlideIndex((prev) => {
//         if (prev >= slides.length - 1) return Math.max(0, slides.length - 2);
//         if (index <= prev) return Math.max(0, prev - 1);
//         return prev;
//       });
//       setSelectedSectionId(null);
    },
    []
  );
//
//   // ===== Layout Change =====
  const changeLayout = useCallback(
    (layout) => {
//       setSlides((prev) =>
//         prev.map((slide, i) =>
//           i === currentSlideIndex
//             ? {
//                 ...slide,
//                 layout,
//                 sections: LAYOUT_TEMPLATES[layout].map((s) => ({ ...s })),
//               }
//             : slide
//         )
//       );
//       setSelectedSectionId(null);
    },
    []
  );
//
//   // ===== Section Selection =====
  const selectSection = useCallback((sectionId) => {
    setSelectedSectionId(sectionId);
  }, []);
//
//   // ===== Section Update (from PropertiesPanel) =====

    const updateSection = useCallback((sectionKey, changes) => {
      setLayout((prev) => ({
        ...prev,
        slots: prev.slots.map((slot) => {
          const key = `${slot.rowPos}-${slot.colPos}`;

          if (key === sectionKey) {
            return {
              ...slot,
              ...changes,
            };
          }

          return slot;
        }),
      }));
    }, []);

//
//   // ===== Slide Reorder =====
  const reorderSlides = useCallback(
    (fromIndex, toIndex) => {
//       setSlides((prev) => {
//         const updated = [...prev];
//         const [moved] = updated.splice(fromIndex, 1);
//         updated.splice(toIndex, 0, moved);
//         return updated;
//       });
//       // Adjust currentSlideIndex to follow the moved slide
//       setCurrentSlideIndex((prev) => {
//         if (prev === fromIndex) return toIndex;
//         if (fromIndex < prev && toIndex >= prev) return prev - 1;
//         if (fromIndex > prev && toIndex <= prev) return prev + 1;
//         return prev;
//       });
    },
    []
  );
//
//   // ===== Preview (stub) =====
  const handlePreview = useCallback(() => {
//     console.log("Preview slides:", JSON.stringify(slides, null, 2));
  }, []);
//
//   // Get currently selected section data for the properties panel
  const getSlotKey = (s) => s.id ?? `${s.rowPos}-${s.colPos}`;

  const selectedSection = layout.slots.find(
    (s) => getSlotKey(s) === selectedSectionId
  );

  return (
    <div className="editor">
      {/* Top Toolbar */}
      <Toolbar
        currentLayout={layout}
        onAddSlide={addSlide}
        onChangeLayout={changeLayout}
        onPreview={handlePreview}
        onSave={handleSave}
        onLogout={() => navigate("/")}


        onAddRow={addRow}
        onAddColumn={addColumn}

        onRemoveRow={removeRow}
        onRemoveColumn={removeColumn}
      />

      <div className="editor-body">
        {/* Left — Slides Panel */}
{/*         <SlidesPanel */}
{/*           slides={layout} */}
{/*           currentSlideIndex={1} */}
{/*           onSwitchSlide={switchSlide} */}
{/*           onAddSlide={addSlide} */}
{/*           onDeleteSlide={deleteSlide} */}
{/*           onReorderSlides={reorderSlides} */}
{/*         /> */}

        {/* Center — Canvas */}
        <div className="canvas-area">
          <div className="canvas-frame">
            <LayoutRenderer
                layout={layout}
                slots={layout.slots}
                selectedSectionId={selectedSectionId}
                onSelectSection={selectSection}
                rows={layout.rows}
                cols={layout.cols}
              />
          </div>
        </div>

        {/* Right — Properties Panel */}
        <PropertiesPanel
          section={selectedSection}
          onUpdate={updateSection}
          sectionKey={selectedSectionId}
        />
      </div>
    </div>
  );
}

export default Canvas;

import { useState, useCallback } from "react";

// ===== SlidesPanel — left sidebar showing slide thumbnails =====

function SlidesPanel({
  slides,
  currentSlideIndex,
  onSwitchSlide,
  onAddSlide,
  onDeleteSlide,
  onReorderSlides,
}) {
  const [dragIndex, setDragIndex] = useState(null);
  const [dropTargetIndex, setDropTargetIndex] = useState(null);

  const handleDragStart = useCallback((e, index) => {
    setDragIndex(index);
    e.dataTransfer.effectAllowed = "move";
    e.dataTransfer.setData("text/plain", index.toString());
  }, []);

  const handleDragOver = useCallback(
    (e, index) => {
      e.preventDefault();
      e.dataTransfer.dropEffect = "move";
      if (index !== dropTargetIndex) {
        setDropTargetIndex(index);
      }
    },
    [dropTargetIndex]
  );

  const handleDragLeave = useCallback(() => {
    setDropTargetIndex(null);
  }, []);

  const handleDrop = useCallback(
    (e, toIndex) => {
      e.preventDefault();
      const fromIndex = dragIndex;
      setDragIndex(null);
      setDropTargetIndex(null);

      if (fromIndex == null || fromIndex === toIndex) return;
      onReorderSlides(fromIndex, toIndex);
    },
    [dragIndex, onReorderSlides]
  );

  const handleDragEnd = useCallback(() => {
    setDragIndex(null);
    setDropTargetIndex(null);
  }, []);

  return (
    <div className="slides-panel">
      <div className="slides-panel-header">
        <span>Slides</span>
        <button onClick={onAddSlide}>+</button>
      </div>

      {slides.map((slide, index) => (
        <div
          key={index}
          className={`slide-thumb${index === currentSlideIndex ? " active" : ""}${dragIndex === index ? " dragging" : ""}${dropTargetIndex === index && dragIndex !== index ? " drop-target" : ""}`}
          onClick={() => onSwitchSlide(index)}
          draggable
          onDragStart={(e) => handleDragStart(e, index)}
          onDragOver={(e) => handleDragOver(e, index)}
          onDragLeave={handleDragLeave}
          onDrop={(e) => handleDrop(e, index)}
          onDragEnd={handleDragEnd}
        >
          {slides.length > 1 && (
            <button
              className="slide-delete"
              title="Delete slide"
              onClick={(e) => {
                e.stopPropagation();
                onDeleteSlide(index);
              }}
            >
              ✕
            </button>
          )}
          <div>Slide {index + 1}</div>
          <div style={{ fontSize: "0.65rem", marginTop: 4, color: "#888" }}>
            {slide.layout}
          </div>
        </div>
      ))}
    </div>
  );
}

export default SlidesPanel;

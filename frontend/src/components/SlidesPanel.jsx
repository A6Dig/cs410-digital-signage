// ===== SlidesPanel — left sidebar showing slide thumbnails =====

function SlidesPanel({ slides, currentSlideIndex, onSwitchSlide, onAddSlide, onDeleteSlide }) {
  return (
    <div className="slides-panel">
      <div className="slides-panel-header">
        <span>Slides</span>
        <button onClick={onAddSlide}>+</button>
      </div>

      {/* FUTURE: Support drag-and-drop reordering */}
      {slides.map((slide, index) => (
        <div
          key={index}
          className={`slide-thumb ${index === currentSlideIndex ? "active" : ""}`}
          onClick={() => onSwitchSlide(index)}
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

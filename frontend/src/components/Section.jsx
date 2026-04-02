import { useState, useEffect } from "react";

// ===== Section — a single content area within a layout =====

// Mock slideshow images for the slideshow content type
const SLIDESHOW_IMAGES = [
  "https://placehold.co/400x200/e2e8f0/64748b?text=Slide+1",
  "https://placehold.co/400x200/dbeafe/3b82f6?text=Slide+2",
  "https://placehold.co/400x200/dcfce7/22c55e?text=Slide+3",
];

function Section({ section, isSelected, onSelect }) {
  return (
    <div
      className={`section ${isSelected ? "selected" : ""}`}
      onClick={() => onSelect(section.id)}
    >
      {section.content ? (
        <div className="section-content">
          <ContentRenderer
            contentType={section.contentType}
            content={section.content}
          />
        </div>
      ) : (
        <span className="section-placeholder">
          Click to add {section.contentType}
        </span>
      )}
    </div>
  );
}

// ===== Content Renderer — renders content based on type =====
function ContentRenderer({ contentType, content }) {
  switch (contentType) {
    case "text":
      return <p>{content}</p>;

    case "image":
      return <img src={content} alt="Section content" />;

    case "video":
      return (
        <iframe
          src={content}
          title="Video content"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope"
          allowFullScreen
        />
      );

    case "slideshow":
      return <SlideshowMock />;

    case "weather":
      return (
        <div className="mock-widget">
          <strong>Weather Widget</strong>
          <br />
          72°F — Sunny
          <br />
          <span style={{ fontSize: "0.75rem", color: "#888" }}>
            Mock data — replace with live API
          </span>
        </div>
      );

    default:
      return <p>{content}</p>;
  }
}

// ===== Slideshow Mock — cycles through placeholder images =====
function SlideshowMock() {
  const [index, setIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setIndex((prev) => (prev + 1) % SLIDESHOW_IMAGES.length);
    }, 2000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="mock-widget">
      <img
        src={SLIDESHOW_IMAGES[index]}
        alt={`Slideshow frame ${index + 1}`}
        style={{ width: "100%", borderRadius: 4 }}
      />
    </div>
  );
}

export default Section;

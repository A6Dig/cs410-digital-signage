import { useState, useEffect, useRef, useCallback } from "react";

// ===== Section — a single content area within a layout =====

// Mock slideshow images for the slideshow content type
const SLIDESHOW_IMAGES = [
  "https://placehold.co/400x200/e2e8f0/64748b?text=Slide+1",
  "https://placehold.co/400x200/dbeafe/3b82f6?text=Slide+2",
  "https://placehold.co/400x200/dcfce7/22c55e?text=Slide+3",
];

function Section({ section, isSelected, onSelect, slotKey, gridStyle = {} }) {
  const sectionRef = useRef(null);
  const [flexPercent, setFlexPercent] = useState(null);

  const handleResizeMouseDown = useCallback((e, direction) => {
    e.preventDefault();
    e.stopPropagation();

    const el = sectionRef.current;
    if (!el) return;

    const parent = el.parentElement;
    const allSections = Array.from(parent.querySelectorAll(":scope > .section"));
    const myIndex = allSections.indexOf(el);
    const nextSection = allSections[myIndex + 1];

    if (!nextSection) return;

    const parentRect = parent.getBoundingClientRect();

    const onMouseMove = (moveE) => {
      const pRect = parent.getBoundingClientRect();

      if (direction === "horizontal") {
        const myStartX =
          el.getBoundingClientRect().left - pRect.left;
        const newWidth = moveE.clientX - pRect.left - myStartX;
        const totalWidth = pRect.width;
        const percent = Math.max(15, Math.min(85, (newWidth / totalWidth) * 100));

        el.style.flex = `0 0 ${percent}%`;
        nextSection.style.flex = `0 0 ${100 - percent}%`;
      } else {
        const myStartY =
          el.getBoundingClientRect().top - pRect.top;
        const newHeight = moveE.clientY - pRect.top - myStartY;
        const totalHeight = pRect.height;
        const percent = Math.max(15, Math.min(85, (newHeight / totalHeight) * 100));

        el.style.flex = `0 0 ${percent}%`;
        nextSection.style.flex = `0 0 ${100 - percent}%`;
      }
    };

    const onMouseUp = () => {
      document.removeEventListener("mousemove", onMouseMove);
      document.removeEventListener("mouseup", onMouseUp);
      document.body.style.cursor = "";
      document.body.style.userSelect = "";

      // Store final value in local state
      const pRect = parent.getBoundingClientRect();
      const elRect = el.getBoundingClientRect();
      if (direction === "horizontal") {
        const percent = (elRect.width / pRect.width) * 100;
        setFlexPercent(Math.max(15, Math.min(85, percent)));
      } else {
        const percent = (elRect.height / pRect.height) * 100;
        setFlexPercent(Math.max(15, Math.min(85, percent)));
      }
    };

    document.body.style.cursor =
      direction === "horizontal" ? "col-resize" : "row-resize";
    document.body.style.userSelect = "none";
    document.addEventListener("mousemove", onMouseMove);
    document.addEventListener("mouseup", onMouseUp);
  }, []);

  const inlineStyle = flexPercent != null
    ? { flex: `0 0 ${flexPercent}%`, overflow: "hidden", ...gridStyle }
    : { overflow: "hidden", ...gridStyle };

  return (
    <div
      ref={sectionRef}
      className={`section ${isSelected ? "selected" : ""}`}
      onClick={() => onSelect(slotKey)}
      style={inlineStyle}
    >
      {section.content ? (
        <div className="section-content">
          <ContentRenderer
            contentType={section.contentType}
            content={section.content}
            fontSize={section.fontSize}
          />
        </div>
      ) : (
        <span className="section-placeholder">
          Click to add {section.contentType}
        </span>
      )}
      <div
        className="resize-handle resize-handle-right"
        onMouseDown={(e) => handleResizeMouseDown(e, "horizontal")}
      />
      <div
        className="resize-handle resize-handle-bottom"
        onMouseDown={(e) => handleResizeMouseDown(e, "vertical")}
      />
    </div>
  );
}

// ===== Content Renderer — renders content based on type =====
function ContentRenderer({ contentType, content, fontSize }) {
  switch (contentType) {
    case "text":
      return <p style={fontSize ? { fontSize: `${fontSize}px` } : undefined}>{content}</p>;

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

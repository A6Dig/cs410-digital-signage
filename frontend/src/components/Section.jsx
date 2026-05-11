import { useState, useEffect, useRef, useCallback, useContext, createContext } from "react";

export const SectionUpdateContext = createContext(null);

const getWeatherInfo = (code) => {
  if (code === 0) return { label: 'Clear Sky', emoji: '☀️' };
  if (code <= 3) return { label: 'Partly Cloudy', emoji: '⛅' };
  if (code <= 48) return { label: 'Foggy', emoji: '🌫️' };
  if (code <= 67) return { label: 'Rainy', emoji: '🌧️' };
  if (code <= 77) return { label: 'Snowy', emoji: '❄️' };
  if (code <= 82) return { label: 'Showers', emoji: '🌦️' };
  if (code <= 99) return { label: 'Thunderstorm', emoji: '⛈️' };
  return { label: 'Unknown', emoji: '🌡️' };
};

function LiveClock() {
  const [now, setNow] = useState(new Date());

  useEffect(() => {
    const interval = setInterval(() => setNow(new Date()), 1000);
    return () => clearInterval(interval);
  }, []);

  const timeStr = now.toLocaleTimeString('en-US', {
    timeZone: 'America/New_York',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false,
  });

  const dateStr = now.toLocaleDateString('en-US', {
    timeZone: 'America/New_York',
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  return (
    <div className="clock-widget">
      <div className="clock-time">{timeStr}</div>
      <div className="clock-date">{dateStr}</div>
      <div className="clock-timezone">Eastern Time (America/New_York)</div>
    </div>
  );
}

function LiveWeather() {
  const [weather, setWeather] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('https://api.open-meteo.com/v1/forecast?latitude=42.3601&longitude=-71.0589&current_weather=true&hourly=relativehumidity_2m&temperature_unit=fahrenheit&windspeed_unit=mph')
      .then(r => r.json())
      .then(data => {
        setWeather(data.current_weather);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="weather-widget">
        <div className="weather-loading">Loading weather...</div>
      </div>
    );
  }

  if (!weather) {
    return (
      <div className="weather-widget">
        <div className="weather-loading">Weather unavailable</div>
      </div>
    );
  }

  const info = getWeatherInfo(weather.weathercode);

  return (
    <div className="weather-widget">
      <div className="weather-emoji">{info.emoji}</div>
      <div className="weather-city">Boston, MA</div>
      <div className="weather-temp">{Math.round(weather.temperature)}°F</div>
      <div className="weather-desc">{info.label}</div>
      <div className="weather-wind">Wind: {Math.round(weather.windspeed)} mph</div>
    </div>
  );
}

function SlideshowCarousel({ sectionId, images, slideDuration: savedDuration, onUpdateImages }) {
  const [localImages, setLocalImages] = useState(images || []);
  const [editing, setEditing] = useState(!images || images.length === 0);
  const [slideDuration, setSlideDuration] = useState(savedDuration || 6);
  const [activeIndex, setActiveIndex] = useState(0);
  const fileInputRef = useRef(null);
  const carouselId = `carousel-section-${sectionId}`;

  useEffect(() => {
    setLocalImages(images || []);
    if (savedDuration) setSlideDuration(savedDuration);
    if (!images || images.length === 0) setEditing(true);
  }, [images, savedDuration]);

  useEffect(() => {
    if (editing || localImages.length <= 1) return;
    const interval = setInterval(() => {
      setActiveIndex(prev => (prev + 1) % localImages.length);
    }, slideDuration * 1000);
    return () => clearInterval(interval);
  }, [editing, localImages.length, slideDuration]);

  const handleFileSelect = (e) => {
    const files = Array.from(e.target.files);
    files.forEach(file => {
      const reader = new FileReader();
      reader.onload = () => {
        setLocalImages(prev => [...prev, { src: reader.result }]);
      };
      reader.readAsDataURL(file);
    });
    e.target.value = '';
  };

  const handleRemoveImage = (index) => {
    setLocalImages(prev => prev.filter((_, i) => i !== index));
  };

  const handleApply = () => {
    if (localImages.length > 0) {
      onUpdateImages(localImages, slideDuration);
      setActiveIndex(0);
      setEditing(false);
    }
  };

  const goToPrev = (e) => {
    e.stopPropagation();
    setActiveIndex(prev => (prev - 1 + localImages.length) % localImages.length);
  };

  const goToNext = (e) => {
    e.stopPropagation();
    setActiveIndex(prev => (prev + 1) % localImages.length);
  };

  if (editing) {
    return (
      <div className="slideshow-edit" onClick={(e) => e.stopPropagation()}>
        <input
          ref={fileInputRef}
          type="file"
          accept="image/*"
          multiple
          onChange={handleFileSelect}
          style={{ display: 'none' }}
        />
        <button className="slideshow-add-btn" onClick={() => fileInputRef.current?.click()}>
          + Add Images
        </button>
        {localImages.length > 0 && (
          <>
            <div className="slideshow-timer-row">
              <label className="slideshow-timer-label">
                Timer:
                <input
                  type="number"
                  min="1"
                  max="60"
                  value={slideDuration}
                  onChange={(e) => setSlideDuration(Math.max(1, Number(e.target.value) || 6))}
                  className="slideshow-duration-input"
                />
                seconds per slide
              </label>
            </div>
            <div className="slideshow-image-list">
              {localImages.map((img, i) => (
                <div key={i} className="slideshow-image-item">
                  <img src={img.src} alt={`Slide ${i + 1}`} className="slideshow-thumb" />
                  <span className="slideshow-image-label">Slide {i + 1}</span>
                  <button className="slideshow-remove-btn" onClick={() => handleRemoveImage(i)}>
                    ×
                  </button>
                </div>
              ))}
            </div>
            <button className="slideshow-apply-btn" onClick={handleApply}>
              Update Carousel
            </button>
          </>
        )}
      </div>
    );
  }

  return (
    <div className="slideshow-carousel-container">
      <div className="slideshow-slides" style={{ height: '100%' }}>
        {localImages.map((img, i) => (
          <div
            key={i}
            className={`slideshow-slide ${i === activeIndex ? 'slideshow-slide-active' : ''}`}
          >
            <img
              src={img.src}
              alt={`Slide ${i + 1}`}
              style={{ width: '100%', height: '100%', objectFit: 'contain', objectPosition: 'center', display: 'block' }}
            />
          </div>
        ))}
      </div>
      {localImages.length > 1 && (
        <>
          <button className="slideshow-control slideshow-control-prev" onClick={goToPrev}>
            &#8249;
          </button>
          <button className="slideshow-control slideshow-control-next" onClick={goToNext}>
            &#8250;
          </button>
        </>
      )}
      <button
        className="slideshow-edit-toggle"
        onClick={(e) => { e.stopPropagation(); setEditing(true); }}
      >
        Edit Images
      </button>
    </div>
  );
}

function Section({ section, isSelected, onSelect, slotKey, gridStyle = {} }) {
  const updateSection = useContext(SectionUpdateContext);
  const sectionRef = useRef(null);

  const handleResizeMouseDown = useCallback((e, direction) => {
    e.preventDefault();
    e.stopPropagation();

    const el = sectionRef.current;
    if (!el) return;

    const parent = el.parentElement;
    if (!parent) return;

    const computedStyle = window.getComputedStyle(parent);
    const trackSizes = (direction === "horizontal"
      ? computedStyle.gridTemplateColumns
      : computedStyle.gridTemplateRows
    ).split(/\s+/).map(parseFloat);

    const gridProp = direction === "horizontal" ? el.style.gridColumn : el.style.gridRow;
    const match = gridProp.match(/(\d+)\s*\/\s*span\s*(\d+)/);
    if (!match) return;

    const pos = parseInt(match[1]);
    const span = parseInt(match[2]);
    const trackIndex = pos + span - 2;

    if (trackIndex < 0 || trackIndex >= trackSizes.length - 1) return;

    const combinedSize = trackSizes[trackIndex] + trackSizes[trackIndex + 1];
    const trackOffset = trackSizes.slice(0, trackIndex).reduce((a, b) => a + b, 0);

    const onMouseMove = (moveE) => {
      const pRect = parent.getBoundingClientRect();
      const mousePos = direction === "horizontal"
        ? moveE.clientX - pRect.left
        : moveE.clientY - pRect.top;

      const newSize = mousePos - trackOffset;
      const minSize = combinedSize * 0.15;
      const maxSize = combinedSize * 0.85;
      const clamped = Math.max(minSize, Math.min(maxSize, newSize));

      const newSizes = [...trackSizes];
      newSizes[trackIndex] = clamped;
      newSizes[trackIndex + 1] = combinedSize - clamped;

      const template = newSizes.map(s => `${s}px`).join(' ');
      if (direction === "horizontal") {
        parent.style.gridTemplateColumns = template;
      } else {
        parent.style.gridTemplateRows = template;
      }
    };

    const onMouseUp = () => {
      document.removeEventListener("mousemove", onMouseMove);
      document.removeEventListener("mouseup", onMouseUp);
      document.body.style.cursor = "";
      document.body.style.userSelect = "";
    };

    document.body.style.cursor = direction === "horizontal" ? "col-resize" : "row-resize";
    document.body.style.userSelect = "none";
    document.addEventListener("mousemove", onMouseMove);
    document.addEventListener("mouseup", onMouseUp);
  }, []);

  const isAutoWidget = ['time', 'weather', 'slideshow'].includes(section.contentType);
  const showContent = section.content || isAutoWidget;

  const inlineStyle = {
    overflow: "hidden",
    ...(isAutoWidget && showContent ? { padding: 0 } : {}),
    ...gridStyle,
  };

  return (
    <div
      ref={sectionRef}
      className={`section ${isSelected ? "selected" : ""}`}
      onClick={() => onSelect(slotKey)}
      style={inlineStyle}
    >
      {showContent ? (
        <div className={isAutoWidget ? "section-widget-content" : "section-content"}>
          <ContentRenderer
            contentType={section.contentType}
            content={section.content}
            fontSize={section.fontSize}
            sectionId={section.id}
            images={section.images}
            slideDuration={section.slideDuration}
            onUpdateImages={(imgs, dur) => updateSection?.(section.id, { images: imgs, slideDuration: dur })}
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

function ContentRenderer({ contentType, content, fontSize, sectionId, images, slideDuration, onUpdateImages }) {
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
      return (
        <SlideshowCarousel
          sectionId={sectionId}
          images={images}
          slideDuration={slideDuration}
          onUpdateImages={onUpdateImages}
        />
      );

    case "weather":
      return <LiveWeather />;

    case "time":
      return <LiveClock />;

    default:
      return <p>{content}</p>;
  }
}

export default Section;

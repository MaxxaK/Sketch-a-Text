const overlay = document.getElementById("overlay");
const overlayImg = document.getElementById("overlay-img");

let scale = 1;
let translateX = 0;
let translateY = 0;

let isDragging = false;
let lastX = 0;
let lastY = 0;

function updateTransform() {
  overlayImg.style.transform =
    ` scale(${scale}) translate(${translateX}px, ${translateY}px)`;
}

function resetView() {
  scale = 1;
  translateX = 0;
  translateY = 0;
  updateTransform();
}

function openPreview() {
    const img = document.getElementById("result");
    const overlay = document.getElementById("overlay");
    const overlayImg = document.getElementById("overlay-img");

    overlayImg.src = img.src;
    overlay.style.display = "flex";
}

function closePreview() {
  overlay.style.display = "none";
}

// ESC to close
window.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    closePreview();
  }
});

// Click outside image to close
overlay.addEventListener("click", (e) => {
  if (e.target === overlay) {
    closePreview();
  }
});

overlay.addEventListener("wheel", (e) => {
  e.preventDefault();

  const zoomIntensity = 0.1;
  const zoomFactor = e.deltaY < 0 ? 1 + zoomIntensity : 1 - zoomIntensity;

  const rect = overlayImg.getBoundingClientRect();
  const cx = e.clientX - rect.left;
  const cy = e.clientY - rect.top;

  translateX -= (cx - rect.width / 2) * (zoomFactor - 1);
  translateY -= (cy - rect.height / 2) * (zoomFactor - 1);

  scale *= zoomFactor;
  scale = Math.min(Math.max(scale, 0.2), 10); // clamp zoom

  updateTransform();
}, { passive: false });

overlayImg.addEventListener("mousedown", (e) => {
  if (scale <= 1) return;

  isDragging = true;
  lastX = e.clientX;
  lastY = e.clientY;
  overlayImg.style.cursor = "grabbing";
});

window.addEventListener("mousemove", (e) => {
  if (!isDragging) return;

  translateX += e.clientX - lastX;
  translateY += e.clientY - lastY;

  lastX = e.clientX;
  lastY = e.clientY;

  updateTransform();
});

window.addEventListener("mouseup", () => {
  isDragging = false;
  overlayImg.style.cursor = "grab";
});

overlayImg.addEventListener("dblclick", resetView);

function render() {
    console.log("Render button clicked");

    document.getElementById("preview-text").style.display = "none";

    const image = document.getElementById("image").files[0];
    const text = document.getElementById("text").value;
    const renderMode = document.querySelector('[name="renderMode"]').value;
    const colorMode = document.querySelector('[name="colorMode"]').value;

    if (!image) {
    alert("Please select an image");
    return;
    }

    if (!text.trim()) {
    alert("Please enter text");
    return;
    }

    const formData = new FormData();
    formData.append("image", image);
    formData.append("text", text);
    formData.append("renderMode", renderMode);
    formData.append("colorMode", colorMode);

    fetch("/render", {
    method: "POST",
    body: formData
    })
    .then(async res => {
    if (!res.ok) {
        const data = await res.json().catch(() => null);
        const message = data?.error || "Rendering failed";
        throw new Error(message);
    }
    return res.blob();
    })
    .then(blob => {
    const img = document.getElementById("result");
    img.src = URL.createObjectURL(blob);
    img.style.display = "block";
    })
    .catch(err => {
    console.error(err);
    alert(err.message);
    });
}

document.getElementById("image").addEventListener("change", e => {
document.getElementById("file-name").textContent =
    e.target.files[0]?.name || "No file selected";
});
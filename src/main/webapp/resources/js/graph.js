function showGraphError(message) {
    const errorDiv = document.getElementById('graphError');
    const errorText = document.getElementById('graphErrorText');
    if (errorDiv && errorText) {
        errorText.textContent = message;
        errorDiv.style.display = 'block';
        setTimeout(() => {
            errorDiv.style.display = 'none';
        }, 5000);
    }
}

function createPointElement(x, y, r, hit) {
    const svg = document.getElementById('svg');
    if (isNaN(x) || isNaN(y) || isNaN(r)) return;

    const svgX = 150 + (x / r) * 100;
    const svgY = 150 - (y / r) * 100;
    if (isNaN(svgX) || isNaN(svgY)) return;

    const point = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    point.setAttribute('cx', svgX);
    point.setAttribute('cy', svgY);
    point.setAttribute('r', '4');
    point.setAttribute('fill', hit ? 'green' : 'red');
    point.setAttribute('class', 'result-point');
    svg.appendChild(point);
}

function clearPoints() {
    const points = document.querySelectorAll('.result-point');
    points.forEach(point => point.remove());
}

function drawLastPoint() {
    clearPoints();
    const rows = document.querySelectorAll('.table tbody tr');
    if (rows.length === 0) return;

    const lastRow = rows[0];
    const cells = lastRow.cells;
    const x = parseFloat(cells[0].textContent || 0);
    const y = parseFloat(cells[1].textContent || 0);
    const r = parseFloat(svg.getAttribute('data-r-value')) || 1;

    if (isNaN(x) || isNaN(y) || isNaN(r) || r === 0) return;
    const hit = cells[4].textContent.includes('✓');
    changeFavicon(hit);
    createPointElement(x, y, r, hit);
}

function handleGraphClick(event) {
    const svg = event.currentTarget;
    const rValue = parseFloat(svg.getAttribute('data-r-value'));

    if (!rValue || rValue === 0) {
        showGraphError('Сначала установите радиус R!');
        return;
    }

    const rect = svg.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    const mathX = ((x - 150) / 100) * rValue;
    const mathY = ((150 - y) / 100) * rValue;

    const validXValues = [-5, -4, -3, -2, -1, 0, 1, 2, 3];
    let closestX = validXValues[0];
    let minDistance = Math.abs(mathX - closestX);

    for (let i = 1; i < validXValues.length; i++) {
        const distance = Math.abs(mathX - validXValues[i]);
        if (distance < minDistance) {
            minDistance = distance;
            closestX = validXValues[i];
        }
    }

    if (minDistance > 0.5) {
        showGraphError('Кликните ближе к значениям X');
        return;
    }
    if (mathY < -3 || mathY > 3) {
        showGraphError('Y должен быть от -3 до 3');
        return;
    }

    document.getElementById('graphForm:graphX').value = closestX;
    document.getElementById('graphForm:graphY').value = mathY.toFixed(2);
    document.getElementById('graphForm:graphSubmit').click();
    setTimeout(drawLastPoint, 500);
}

function changeFavicon(isHit) {
    const favicon = document.getElementById('favicon');
    const iconName = isHit ? 'hit.ico' : 'miss.ico';
    favicon.href = `jakarta.faces.resource/images/${iconName}.xhtml`;
    setTimeout(() => {
        favicon.href = `jakarta.faces.resource/images/favicon.ico.xhtml`;
    }, 10000);
}

function updateRadiusLabels(r) {
    const svg = document.getElementById('svg');
    if (!svg) return;
    const labels = document.querySelectorAll('text');
    labels[2].textContent = -r;
    labels[3].textContent = -(r/2).toFixed(1);
    labels[4].textContent = (r/2).toFixed(1);
    labels[5].textContent = r;
    labels[6].textContent = r;
    labels[7].textContent = (r/2).toFixed(1);
    labels[8].textContent = -(r/2).toFixed(1);
    labels[9].textContent = -r;
    svg.setAttribute('data-r-value', r);
}

function handleRadiusChange(r) {
    updateRadiusLabels(r);
    drawLastPoint();
}

document.addEventListener('DOMContentLoaded', function() {
    const svg = document.getElementById('svg');
    if (svg) {
        svg.addEventListener('click', handleGraphClick);
        const initialR = parseFloat(svg.getAttribute('data-r-value')) || 1;
        updateRadiusLabels(initialR);
        setTimeout(drawLastPoint, 100);
    }

    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('glass-option')) {
            const parent = e.target.closest('.choice-x, .choice-r');
            if (parent) {
                parent.querySelectorAll('.glass-option').forEach(btn => {
                    btn.classList.remove('active', 'selected');
                });
            }
            e.target.classList.add('active', 'selected');

            if (parent && parent.className.includes('choice-r')) {
                const buttons = parent.querySelectorAll('.glass-option');
                const index = Array.from(buttons).indexOf(e.target);
                const rValues = [1, 1.5, 2, 2.5, 3];
                const rValue = rValues[index];

                if (!isNaN(rValue) && rValue > 0) {
                    updateRadiusLabels(rValue);
                    drawLastPoint();
                }
            }
        }
    });
});
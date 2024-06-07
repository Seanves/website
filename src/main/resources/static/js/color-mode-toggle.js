
const colorVars = [
    "--navbar-color",
    "--sidebar-color",
    "--sidebar-button-color",
    "--navbar-items-color",
    "--in-logo-droplist-color",
    "--auth-color"
];


function toggleColorMode() {
    let isLight = toggleAndGetIsLightMode();

    changeColors(isLight)
    updateToggleButton(isLight);
}

function onStart() {
    let isLight = isLightMode();
    changeColors(isLight)
}

function changeColors(isLight) {
    mode = isLight ? "-light)" : "-dark)";

    for (let colorVar of colorVars) {
        document.documentElement.style.setProperty(
            colorVar,
            "var(" + colorVar + mode
        );
    }


}

function updateToggleButton(isLight) {
    let svgImage = document.getElementById('colorModeToggleSvg');
    svgImage.src = isLight ? "images/light-mode.svg" : "images/dark-mode.svg";
}

function isLightMode() {
    let isLight = getCookie("isLightMode");
    if (isLight === undefined) {
        setCookie("isLightMode", "false")
        return false;
    }

    return isLight === "true";
}

function toggleAndGetIsLightMode() {
    let toggled = !isLightMode();
    setCookie("isLightMode", toggled);
    return toggled;
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function setCookie(name, value) {
    var expires = "";
    var date = new Date();
    date.setTime(date.getTime() + (/* month */ 30 * 24 * 60 * 60 * 1000));
    expires = "; expires=" + date.toUTCString();
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}


window.onload = function() {
    updateToggleButton(isLightMode())
};

onStart()
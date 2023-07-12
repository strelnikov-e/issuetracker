const light = 50;
const saturation = 50;

export const UserBadge = ({ user }) => {
  const firstName = user?.firstName ? user.firstName.charAt(0) : "";
  const lastName = user?.lastName ? user.lastName.charAt(0) : "";
  const initials = firstName + lastName || "?";


  const color = stringToHslColor(initials, saturation, light );

  return (
    <>
      <span
        className="usercircle me-2"
        style={{
          backgroundColor: color,
          verticalAlign: "middle",
          display: "inline-block",
        }}
      >
        <p className="usercircle-inner text-center">{initials}</p>
      </span>
      <span
        style={{ verticalAlign: "middle", display: "inline-block" }}
        className=""
      >
        {initials === "?" ? "Unasigned" : user.firstName + " " + user.lastName}
      </span>
    </>
  );
};

export const UserBadgeLarge = ({ user }) => {
  const firstName = user?.firstName ? user.firstName.charAt(0) : "";
  const lastName = user?.lastName ? user.lastName.charAt(0) : "";
  const initials = firstName + lastName || "?";

  const name1 = user?.firstName ? user.firstName : ""
  const name2 = user?.lastName ? user.lastName : "";
  const colorName = name1 + name2 || "?"

  const color = stringToHslColor(initials, saturation, light );

  return (
    <>
      <span
        className="usercircle me-2"
        style={{
          backgroundColor: color,
          verticalAlign: "middle",
          display: "inline-block",
        }}
      >
        <p className="usercircle-inner text-center">{initials}</p>
      </span>
      <span
        style={{ verticalAlign: "middle", display: "inline-block" }}
        className="fs-5"
      >
        {initials === "?" ? "Unasigned" : user.firstName + " " + user.lastName}
      </span>
    </>
  );
};

export const UserBadgeOnly = ({ user }) => {
  const firstName = user?.firstName ? user.firstName.charAt(0) : "";
  const lastName = user?.lastName ? user.lastName.charAt(0) : "";
  const initials = firstName + lastName || "?";

  // const name1 = user?.firstName ? user.firstName : ""
  // const name2 = user?.lastName ? user.lastName : "";
  // const colorName = name1 + name2 || "?"

  const color = stringToHslColor(initials, saturation, light );

  return (
    <>
      <span
        className="usercircle me-2"
        style={{
          backgroundColor: color,
          verticalAlign: "middle",
          display: "inline-block",
        }}
      >
        <p className="usercircle-inner">{initials}</p>
      </span>
    </>
  );
};

export const UserBadgeSmOnly = ({ user }) => {
  const firstName = user?.firstName ? user.firstName.charAt(0) : "";
  const lastName = user?.lastName ? user.lastName.charAt(0) : "";
  const initials = firstName + lastName || "?";

  const color = stringToHslColor(initials, saturation, light );

  return (
    <>
      <span
        className="usercircle mb-1"
        style={{
          backgroundColor: color,
          verticalAlign: "middle",
          display: "inline-block",
          width: 22,
          height: 22
        }}
      >
        <p
          className=" usercircle-inner text-center"
          style={{ fontSize: 10, height: 22}}
        >
          {initials}
        </p>
      </span>
    </>
  );
};

// s - saturation (0-100)
// l - lightness (0-100)
function stringToHslColor(str, s, l) {
  var hash = 0;
  if (str === "?") {
    return "hsl(" + hash + ", " + 0 + "%, " + 70 + "%)";
  }

  for (var i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + hash<<4;
  }

  var h = hash % 360;
  return "hsl(" + h + ", " + s + "%, " + l + "%)";
}

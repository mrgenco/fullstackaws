const ImageCard = ({ image }) => {
  const tags = image.tags.split(",");

  return (
    <div className="max-w-sm rounded overflow-hidden shadow-lg">
      <img
        className="object-cover h-48 w-full"
        src={"http://localhost:8080/image/download/" + image.imageId}
        alt=""
      />
      <div className="px-6 py-4">
        <div className="font-bold text-purple-500 text-xl mb-2">
          Photo name : {image.fileName}
        </div>
        <ul>
          <li>
            <strong>Size: </strong>
            {image.fileSize}KB
          </li>
          <li>
            <strong>Description: </strong>
            {image.fileDesc}
          </li>

          <li>
            <strong>Created Date: </strong>
            {image.createDate}
          </li>
        </ul>
      </div>
      <div className="px-6 py-2">
        <strong>Tags: </strong>
        {tags.map((tag, index) => (
          <span
            key={index}
            className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2"
          >
            #{tag}
          </span>
        ))}
      </div>
    </div>
  );
};

export default ImageCard;

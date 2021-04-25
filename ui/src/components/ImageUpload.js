const ImageUpload = () => {
  const onClick = () => {
    console.log("Upload button clicked");
  };
  return (
    <div className="max-w-sm rounded content-center  mt-5 mb-20 items-center mx-auto ">
      <button
        className="w-full inline-flex justify-center py-2 px-6 border border-transparent shadow-sm text-md font-medium rounded-md text-white bg-purple-500 hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500"
        onClick={onClick}
      >
        Upload New Image
      </button>
    </div>
  );
};

export default ImageUpload;

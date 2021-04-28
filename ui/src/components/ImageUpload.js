import { useState } from "react";
const ImageUpload = () => {
    const [isUploadClicked, setUploadClicked] = useState(false);
    const [selectedFile, setSelectedFile] = useState("");
    const upload = () => {
        console.log("Upload button clicked");
        setUploadClicked(true);
    };
    const cancel = () => {
        console.log("Cancel button clicked");
        setUploadClicked(false);
    };
    const onSubmit = (e) => {
        e.preventDefault();
    };
    const onFileChange = (e) => {
        // Update the state        
        console.log("FileName : " + e.target.files[0].name);
        setSelectedFile( e.target.files[0]);
        
    };
    return (
        <div className="max-w-sm rounded content-center  mt-5 mb-20 items-center mx-auto ">
            <button
                className="w-full inline-flex justify-center py-2 px-6 border border-transparent shadow-sm text-md font-medium rounded-md text-white bg-purple-500 hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500"
                onClick={upload}
            >
                Upload New Image
            </button>
            {isUploadClicked && (
                <div>
                    <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true"></div>
                    <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
                        <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
                            <div className="sm:flex sm:items-start">
                                <div className="w-full mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                                    <div className="mt-2">

                                        <form onSubmit={onSubmit} className="mb-4 w-full" action="/" method="post">
                                            <div className="flex flex-col mb-4 w-full">
                                                <label className="mb-2 font-bold text-lg  text-purple-500" >Image Description</label>
                                                <input placeholder="Image Description" className="bg-gray-100  rounded py-2 px-3 " type="text" name="first_name" id="first_name"></input>
                                            </div>
                                            <div className="flex flex-col mb-4 w-full">
                                                <label className="mb-2 font-bold text-lg text-purple-500" >Image Tags</label>
                                                <input placeholder="Ex: tag1, tag2, tag3" className="bg-gray-100 rounded py-2 px-3 " type="text" name="last_name" id="last_name"></input>
                                            </div>
                                            <div className="flex flex-col mb-4 w-full">
                                                <input  type="file" onChange={onFileChange}></input>
                                            </div>
                                            
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
                            <button type="submit" className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-purple-500 text-base font-medium text-white hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-300 sm:ml-3 sm:w-auto sm:text-sm">
                                Save
                            </button>
                            <button onClick={cancel} type="button" className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ImageUpload;

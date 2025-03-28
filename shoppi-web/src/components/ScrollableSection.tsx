import React, {useEffect, useRef} from "react";

type ScrollableSectionProps = {
    children: React.ReactNode;
};

const SCROLL_SPEED = 8;

const ScrollableSection: React.FC<ScrollableSectionProps> = ({children}) => {
    const containerRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const container = containerRef.current;
        if (!container) return;

        const handleWheel = (e: WheelEvent) => {
            if (container.scrollWidth > container.clientWidth) {
                e.preventDefault();
                container.scrollLeft += e.deltaY * SCROLL_SPEED;

            }
        };

        container.addEventListener("wheel", handleWheel, {passive: false});

        return () => {
            container.removeEventListener("wheel", handleWheel);
        };
    }, []);

    const disablePageScroll = () => {
        document.body.style.overflow = "hidden";
    };

    const enablePageScroll = () => {
        document.body.style.overflow = "";
    };

    return (
        <div
            ref={containerRef}
            className="overflow-x-auto overflow-y-hidden scroll-smooth"
            style={{scrollbarGutter: "stable"}}
            onMouseEnter={disablePageScroll}
            onMouseLeave={enablePageScroll}
        >
            <div className="flex space-x-6 px-6 pb-4">{children}</div>
        </div>
    );
};

export default ScrollableSection;
